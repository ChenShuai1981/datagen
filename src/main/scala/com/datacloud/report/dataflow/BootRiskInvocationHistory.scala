package com.datacloud.report.dataflow

import java.util.Properties

import com.datacloud.polaris.protocol.avro.RiskInvocationHistory
import com.datacloud.report.dataflow.riskinvocationhistory.RiskInvocationHistoryCreator
import com.datacloud.report.dataflow.riskinvocationhistory.RiskInvocationHistoryProducer.{interval, topic}
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object BootRiskInvocationHistory {

  def main(args: Array[String]) = {
    if (args.length < 3) {
      println("run with parameters: BootRiskInvocationHistory [kafkaBrokers] [schemaRegistryUrl] [topicName]")
      System.exit(0)
    }

    val kafkaBrokers = args(0) // localhost:9092
    val schemaRegistryUrl = args(1) // http://localhost:8081
    val topicName = args(2) // mytopic

    val props = new Properties

    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
    props.put(ProducerConfig.ACKS_CONFIG, "all")
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
    props.put("schema.registry.url", schemaRegistryUrl)

    val producer: Producer[String, RiskInvocationHistory] = new KafkaProducer[String, RiskInvocationHistory](props)
    val riskInvocationHistories = RiskInvocationHistoryCreator.getRiskInvocationHistories
    riskInvocationHistories.foreach(riskInvocationHistory => {
      val now = System.currentTimeMillis()
      riskInvocationHistory.setOccurTime(now)
      riskInvocationHistory.setTenantId(29L)
      riskInvocationHistory.setEventCode("loanApply")
      riskInvocationHistory.setProductCode("TEST")
      producer.send(new ProducerRecord[String, RiskInvocationHistory](topicName, null, riskInvocationHistory.getOccurTime, riskInvocationHistory.getTenantId + "|" + String.valueOf(riskInvocationHistory.getExecutionId), riskInvocationHistory))
      import scala.collection.JavaConversions._
      val ruleIds = riskInvocationHistory.getAntifraudDetail.getHitRuleSets.flatMap(ruleSet => ruleSet.getHitRules.map(rule => rule.getRuleId)).toArray
      //.map(hitRule => hitRule.).collect(Collectors.toList)
      println(now + "|" + ruleIds.mkString(",") + " => " + riskInvocationHistory.toString)
      Thread.sleep(interval)
    })

    producer.flush()
    producer.close()
  }

}
