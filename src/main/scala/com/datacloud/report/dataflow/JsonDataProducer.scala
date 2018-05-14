package com.datacloud.report.dataflow

import java.util.Properties

import com.datacloud.report.dataflow.util.JsonUtil
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.clients.producer.ProducerConfig

trait JsonDataProducer[T] extends DataCreator[T] {
  def getTopic: String
  def getBootstrap: String
  def getInterval: Long
  def getLoop: Int

  val props = new Properties
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  props.put(ProducerConfig.ACKS_CONFIG, "all")
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrap)

  val producer: Producer[String, String] = new KafkaProducer[String, String](props)
  for (i <- 1 to getLoop) {
    getDatas.foreach(data => {
      val jsonString = JsonUtil.toJson(data)
      producer.send(
        new ProducerRecord[String, String](getTopic, null, String.valueOf(System.currentTimeMillis()), jsonString))
      println(jsonString)
      Thread.sleep(getInterval)
    })
  }

  producer.flush()
  producer.close()
}
