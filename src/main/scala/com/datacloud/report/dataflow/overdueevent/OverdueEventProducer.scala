package com.datacloud.report.dataflow.overdueevent

import java.util.Properties

import com.datacloud.polaris.protocol.avro.OverdueEvent
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object OverdueEventProducer extends App {

  val topic = "uat_OVERDUE_EVENT"

  val props = new Properties
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
  props.put(ProducerConfig.ACKS_CONFIG, "all")
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.201.9.76:9092")
  props.put("schema.registry.url", "http://10.201.9.76:8081")

//  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ambari-agent4.sit.geerong.com:9092")
//  props.put("schema.registry.url", "http://ambari-agent4.sit.geerong.com:8081")

  val producer: Producer[String, OverdueEvent] = new KafkaProducer[String, OverdueEvent](props)
  //  val max = Integer.MAX_VALUE
//  val max = 10000
  val interval = 500

  val overdueEvents = OverdueEventCreator.getOverdueEvents
//  var i = 1L
  overdueEvents.foreach(overdueEvent => {
    overdueEvent.setTenantId(29L)
    overdueEvent.setProductCode("TEST")
    overdueEvent.setTerminal("GENERAL")
//    overdueEvent.setRiskProcessId(1234567422L)
//    i = i+1
    producer.send(
      new ProducerRecord[String, OverdueEvent](topic, null, overdueEvent.getEventTime, overdueEvent.getTenantId+"|"+String.valueOf(overdueEvent.getRiskProcessId), overdueEvent))
    println(overdueEvent.toString)
    Thread.sleep(interval)
  })

  producer.close()

}
