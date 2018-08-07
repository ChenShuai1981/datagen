package com.datacloud.datagen

import java.util.Properties

import com.datacloud.datagen.util.JsonUtil
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.clients.producer.ProducerConfig
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks

abstract class JsonDataProducer[T](topicName: String,
                                   bootstrapServers: String = "localhost:9092",
                                   interval: Long = 60, loop: Int = 10) extends DataProducer with GeneratorDrivenPropertyChecks {

  def genData: Gen[T]

  def getKey(t: T): String

  def run() = {
    val props = new Properties
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.ACKS_CONFIG, "all")
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)

    val producer: Producer[String, String] = new KafkaProducer[String, String](props)
    for (i <- 1 to loop) {
      forAll(genData) {
        (data: T) => {
          val jsonString = JsonUtil.toJson(data)
          println(jsonString)
          producer.send(new ProducerRecord[String, String](topicName, null, getKey(data), jsonString))
          Thread.sleep(interval)
        }
      }
    }

    producer.flush()
    producer.close()
  }
}
