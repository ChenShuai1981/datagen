package com.datacloud.report.dataflow.riskinvocationhistory

import java.util.Properties
import java.util.stream.Collectors

import com.datacloud.polaris.protocol.avro.{AntifraudHitRuleSet, RiskInvocationHistory}
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object RiskInvocationHistoryProducer extends App {

  val topic = "sit_RISK_INVOCATION_HISTORY"

  val props = new Properties
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
  props.put(ProducerConfig.ACKS_CONFIG, "all")

//  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
//  props.put("schema.registry.url", "http://localhost:8081")

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ambari-agent4.sit.geerong.com:9092")
  props.put("schema.registry.url", "http://ambari-agent4.sit.geerong.com:8081")

  val producer: Producer[String, RiskInvocationHistory] = new KafkaProducer[String, RiskInvocationHistory](props)
  val interval = 500
  val loop = 500
  for (i <- 1 to loop) {
    val riskInvocationHistories = RiskInvocationHistoryCreator.getRiskInvocationHistories
    riskInvocationHistories.foreach(riskInvocationHistory => {
      val now = System.currentTimeMillis()
      riskInvocationHistory.setOccurTime(now)
      riskInvocationHistory.setTenantId(29L)
      riskInvocationHistory.setEventCode("loanApply")
      riskInvocationHistory.setProductCode("TEST")
      producer.send(new ProducerRecord[String, RiskInvocationHistory](topic, null, riskInvocationHistory.getOccurTime, riskInvocationHistory.getTenantId + "|" + String.valueOf(riskInvocationHistory.getExecutionId), riskInvocationHistory))
      import scala.collection.JavaConversions._
      val ruleIds = riskInvocationHistory.getAntifraudDetail.getHitRuleSets.flatMap(ruleSet => ruleSet.getHitRules.map(rule => rule.getRuleId)).toArray
        //.map(hitRule => hitRule.).collect(Collectors.toList)
      println(now + "|" + ruleIds.mkString(",") + " => " + riskInvocationHistory.toString)
      Thread.sleep(interval)
    })
  }
  producer.close()

}
