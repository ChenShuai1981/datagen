package com.datacloud.report.dataflow.overdueevent

import java.util.Properties

import com.datacloud.report.dataflow.creditinvocationhistory.CreditInvocationHistoryCreator
import com.datacloud.report.dataflow.util.JsonUtil
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object OverdueEventWithCreditInvocationHistoryProducer extends App {

  val overdueEventTopic = "OVERDUE_EVENT2"
  val creditInvocationHistoryTopic = "CREDIT_INVOCATION_HISTORY2"

  val props = new Properties
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "bigdata-hbase2.sit.geerong.com:9094")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
  props.put("schema.registry.url", "http://bigdata-hbase2.sit.geerong.com:8084")
  props.put("acks", "all")

  val producer: Producer[String, String] = new KafkaProducer[String, String](props)
  val interval = 1000

  val begin = System.currentTimeMillis()
  val overdueEvents = OverdueEventCreator.getOverdueEvents.slice(0, 10)
  var riskProcessId = begin
  overdueEvents.foreach(overdueEvent => {
    overdueEvent.setRiskProcessId(riskProcessId)
    println(JsonUtil.toJson(overdueEvent))
    // send overdue event
    producer.send(
      new ProducerRecord[String, String](overdueEventTopic, null, overdueEvent.getEventTime, overdueEvent.getTenantId + "|" + String.valueOf(overdueEvent.getRiskProcessId), JsonUtil.toJson(overdueEvent)))

    val random = new java.util.Random()
    val num = 1 + random.nextInt(5) // at most 5 strategies
    val creditInvocationHistories = CreditInvocationHistoryCreator.getCreditInvocationHistories
    val sublist = creditInvocationHistories.slice(0, num)
    for (creditInvocationHistory <- sublist) {
      creditInvocationHistory.setTenantId(overdueEvent.getTenantId)
      creditInvocationHistory.setTerminal(overdueEvent.getTerminal)
      creditInvocationHistory.setProductCode(overdueEvent.getProductCode)

      creditInvocationHistory.setRiskProcessId(riskProcessId)
      creditInvocationHistory.setOccurTime(overdueEvent.getDueDate - 24 * 60 * 60 * 1000)
      println(JsonUtil.toJson(creditInvocationHistory))
      // send credit invocation history
      producer.send(
        new ProducerRecord[String, String](creditInvocationHistoryTopic, null, creditInvocationHistory.getOccurTime, creditInvocationHistory.getTenantId + "|" + String.valueOf(creditInvocationHistory.getExecutionId), JsonUtil.toJson(creditInvocationHistory)))
    }
    println()
    riskProcessId = riskProcessId + 1
    Thread.sleep(interval)
  })

  producer.close()

}
