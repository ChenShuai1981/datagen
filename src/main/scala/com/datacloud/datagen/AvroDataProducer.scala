package com.datacloud.datagen

import java.util.Properties

import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks

trait KafkaEnv {
  //  val envPrefix = "local_"
  //  val bootstrapServers = "localhost:9092"
  //  val schemaRegistryUrl = "http://localhost:8081"

  val envPrefix = "feature1_"
  val bootstrapServers = "10.12.0.161:9092"
  val schemaRegistryUrl = "http://10.12.0.161:8081"

//    val envPrefix = "dev_"
//    val bootstrapServers = "10.12.0.210:9092"
//    val schemaRegistryUrl = "http://10.12.0.210:8081"

//    val envPrefix = "sit_"
//    val bootstrapServers = "10.12.0.131:9092"
//    val schemaRegistryUrl = "http://10.12.0.131:8081"

  //  val envPrefix = "preprod_"
  //  val bootstrapServers = "10.12.0.6:9092"
  //  val schemaRegistryUrl = "http://10.12.0.6:8081"

//    val envPrefix = "preprod_"
//    val bootstrapServers = "10.12.0.175:9092"
//    val schemaRegistryUrl = "http://10.12.0.175:8081"
}

abstract class AvroDataProducer[T](topicName: String,
                                   bootstrapServers: String = "localhost:9092",
                                   schemaRegistryUrl: String = "http://localhost:8081",
                                   interval: Long = 60, loop: Int = 10) extends DataProducer with GeneratorDrivenPropertyChecks {

  def genData: Gen[T]

  def getKey(t: T): String

  def run() {
    val props = new Properties
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
    props.put(ProducerConfig.ACKS_CONFIG, "all")
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.put("schema.registry.url", schemaRegistryUrl)
    props.put("enable.idempotence", "true")

    val producer: Producer[String, T] = new KafkaProducer[String, T](props)
    for (i <- 1 to loop) {
      forAll(genData) {
        (data: T) => {
          producer.send(new ProducerRecord[String, T](topicName, getKey(data), data))
          println(data.toString)
          Thread.sleep(interval)
        }
      }
    }

    producer.flush()
    producer.close()
  }
}
