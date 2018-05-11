package com.datacloud.report.dataflow.market

import java.util.Properties

import com.datacloud.report.dataflow.util.JsonUtil
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object MarketDecisionResultProducer {

  def main(args: Array[String]) = {
    val topic = "dev_MARKET_DECISION_RESULT"

    val props = new Properties

    import io.confluent.kafka.serializers.KafkaAvroSerializer
    import org.apache.kafka.clients.producer.ProducerConfig

    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.ACKS_CONFIG, "all")

    //  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ambari-agent4.sit.geerong.com:9092")
    props.put("schema.registry.url", "http://ambari-agent4.sit.geerong.com:8081")

    val producer: Producer[String, String] = new KafkaProducer[String, String](props)
    val interval = 50
    val marketDecisionResults = MarketDecisionResultCreator.getMarketDecisionResults
    marketDecisionResults.foreach(marketDecisionResult => {
      val jsonString = JsonUtil.toJson(marketDecisionResult)
      producer.send(
        new ProducerRecord[String, String](topic, null, marketDecisionResult.transactionTime, marketDecisionResult.marketProcessId, jsonString))
      println(jsonString)
      Thread.sleep(interval)
    })

    producer.flush()
    producer.close()
  }

}
