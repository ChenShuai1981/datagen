package com.datacloud.report.dataflow.creditinvocationhistory

import java.util.Properties

import com.datacloud.polaris.protocol.avro.CreditInvocationHistory
import com.datacloud.report.dataflow.overdueevent.OverdueEventProducer.props
import com.datacloud.report.dataflow.riskinvocationhistory.RiskInvocationHistoryProducer.props
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object CreditInvocationHistoryProducer extends App {

  val topic = "sit_CREDIT_INVOCATION_HISTORY"

  val props = new Properties

  import io.confluent.kafka.serializers.KafkaAvroSerializer
  import org.apache.kafka.clients.producer.ProducerConfig

  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
  props.put(ProducerConfig.ACKS_CONFIG, "all")
//  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.201.9.76:9092")
//  props.put("schema.registry.url", "http://10.201.9.76:8081")

//  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
//  props.put("schema.registry.url", "http://localhost:8081")

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ambari-agent4.sit.geerong.com:9092")
  props.put("schema.registry.url", "http://ambari-agent4.sit.geerong.com:8081")

  val producer: Producer[String, CreditInvocationHistory] = new KafkaProducer[String, CreditInvocationHistory](props)
  //  val max = Integer.MAX_VALUE
//  val max = 100000
  val interval = 500
//  var riskProcessId = 1L
  val creditInvocationHistories = CreditInvocationHistoryCreator.getCreditInvocationHistories
  creditInvocationHistories.foreach(creditInvocationHistory => {
//    creditInvocationHistory.setRiskProcessId(riskProcessId)
    producer.send(
      new ProducerRecord[String, CreditInvocationHistory](topic, null, creditInvocationHistory.getOccurTime, creditInvocationHistory.getTenantId+"|"+String.valueOf(creditInvocationHistory.getExecutionId), creditInvocationHistory))
    println(creditInvocationHistory.toString)
//    riskProcessId = riskProcessId + 1
    Thread.sleep(interval)
  })

  producer.flush()
  producer.close()

}
