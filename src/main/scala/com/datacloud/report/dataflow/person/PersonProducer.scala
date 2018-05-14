package com.datacloud.report.dataflow.person

import java.util.Properties

import com.datacloud.report.dataflow.protocol.avro.Person
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object PersonProducer extends App {

  val topic = "person2"

  val props = new Properties
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
  props.put(ProducerConfig.ACKS_CONFIG, "all")
  //  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.201.9.76:9092")
  //  props.put("schema.registry.url", "http://10.201.9.76:8081")

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put("schema.registry.url", "http://localhost:8081")

  val producer: Producer[String, Person] = new KafkaProducer[String, Person](props)
  //  val max = Integer.MAX_VALUE
  //  val max = 10000
  val interval = 50

  val persons = PersonCreator.getPersons
  persons.foreach(person => {
    producer.send(
      new ProducerRecord[String, Person](topic, null, person.getTimestampMillis, person.getName, person))
    println(person.toString)
    Thread.sleep(interval)
  })

  producer.close()

}
