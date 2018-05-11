package com.datacloud.report.dataflow.country

import java.util.Properties

import com.datacloud.report.dataflow.protocol.avro.Country
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object CountryProducer extends App {

  val topic = "COUNTRY2"

  val props = new Properties

  import io.confluent.kafka.serializers.KafkaAvroSerializer
  import org.apache.kafka.clients.producer.ProducerConfig

  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
  props.put("acks", "all")
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put("schema.registry.url", "http://localhost:8081")

  val producer: Producer[String, Country] = new KafkaProducer[String, Country](props)
  val interval = 2000
  val countries = CountryCreator.getCountries
  countries.foreach(country => {
    producer.send(
      new ProducerRecord[String, Country](topic, country))
    println(country)
    Thread.sleep(interval)
  })

  producer.flush()
  producer.close()
}
