package com.datacloud.report.dataflow.person

import java.util.{Collections, Properties}

import com.datacloud.report.dataflow.protocol.avro.Person
import io.confluent.kafka.serializers.{AbstractKafkaAvroSerDeConfig, KafkaAvroDeserializer, KafkaAvroDeserializerConfig}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

object PersonConsumer extends App {

  val topic = "person"

  val props = new Properties
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer].getName)
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "PersonConsumer")
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put("specific.avro.reader", "true")
  props.put("schema.registry.url", "http://localhost:8081"); //<----- Run Schema Registry on 8081

  val consumer = new KafkaConsumer[String, Person](props)

  consumer.subscribe(Collections.singletonList(topic))

  val records: ConsumerRecords[String, Person] = consumer.poll(100)

  if (records.count == 0) {
    System.out.println("None found")
  } else {
    val it = records.iterator()
    while(it.hasNext) {
      val record: ConsumerRecord[String, Person] = it.next()
      val person = record.value()
      println(person)
    }
  }
}
