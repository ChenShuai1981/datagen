package com.datacloud.report.dataflow

import java.util.Properties

import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

trait AvroDataProducer[T] extends DataCreator[T] {
  def getTopic: String
  def getBootstrap: String
  def getSchemaRegistryUrl: String
  def getInterval: Long
  def getLoop: Int

  val props = new Properties
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
  props.put(ProducerConfig.ACKS_CONFIG, "all")
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrap)
  props.put("schema.registry.url", getSchemaRegistryUrl)

  val producer: Producer[String, T] = new KafkaProducer[String, T](props)
  for (i <- 1 to getLoop) {
    getDatas.foreach(data => {
      producer.send(
        new ProducerRecord[String, T](getTopic, data))
      println(data.toString)
      Thread.sleep(getInterval)
    })
  }

  producer.flush()
  producer.close()
}
