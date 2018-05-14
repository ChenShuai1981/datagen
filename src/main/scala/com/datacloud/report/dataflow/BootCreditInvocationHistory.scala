package com.datacloud.report.dataflow

import java.util.Properties

import com.datacloud.polaris.protocol.avro.CreditInvocationHistory
import com.datacloud.report.dataflow.creditinvocationhistory.CreditInvocationHistoryCreator
import org.apache.kafka.common.serialization.StringSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}

object BootCreditInvocationHistory {

  def main(args: Array[String]) = {
    if (args.length < 3) {
      println("run with parameters: BootCreditInvocationHistory [kafkaBrokers] [schemaRegistryUrl] [topicName]")
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

    val producer: Producer[String, CreditInvocationHistory] = new KafkaProducer[String, CreditInvocationHistory](props)
    val creditInvocationHistories = CreditInvocationHistoryCreator.getCreditInvocationHistories
    creditInvocationHistories.foreach(creditInvocationHistory => {
      producer.send(
        new ProducerRecord[String, CreditInvocationHistory](topicName, null, creditInvocationHistory.getOccurTime, creditInvocationHistory.getTenantId+"|"+String.valueOf(creditInvocationHistory.getExecutionId), creditInvocationHistory))
      println(creditInvocationHistory.toString)
    })

    producer.flush()
    producer.close()
  }

}
