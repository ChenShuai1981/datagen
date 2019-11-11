//package com.datacloud.datagen.history
//
//import com.datacloud.datagen.AvroDataProducer
//import com.datacloud.polaris.protocol.avro._
//import org.scalacheck.Gen
//import scala.collection.JavaConversions._
//
//object CreditNodeHistoryProducer extends App {
//  val topicName = "dev_CREDIT_NODE_HISTORY"
//  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"
//  val schemaRegistryUrl = "http://ambari-agent4.sit.geerong.com:8081"
//  //  val bootstrapServers = "localhost:9092"
//  //  val schemaRegistryUrl = "http://localhost:8081"
//
//  val producer = new CreditNodeHistoryProducer(topicName, bootstrapServers, schemaRegistryUrl, 60L, 1)
//  producer.run()
//}
//
//class CreditNodeHistoryProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
//  extends AvroDataProducer[CreditNodeHistory](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {
//
//  def genData: Gen[CreditNodeHistory] = {
//    for {
//      executionId <- Gen.uuid.map(uuid => Math.abs(uuid.getMostSignificantBits + uuid.getLeastSignificantBits))
//      creditStrategyId <- Gen.choose(100L, 300L)
//      tenantId <- Gen.oneOf(Seq(29L))
//      userId <- Gen.const(100L)
//      occurTime <- Gen.choose(1000, 10*60*1000).map(lag => System.currentTimeMillis() - lag)
//      input <- genInput
//      procInstanceId <- Gen.identifier
//      actId <- Gen.identifier
//      foreignType <- Gen.identifier
//      foreignId <- Gen.choose(1234L, 4567L)
//    } yield {
//      CreditNodeHistory.newBuilder()
//        .setExecutionId(executionId)
//        .setCreditStrategyId(creditStrategyId)
//        .setTenantId(tenantId)
//        .setUserId(userId)
//        .setOccurTime(occurTime)
//        .setInput(input)
//        .setOutput(input)
//        .setProcInstanceId(procInstanceId)
//        .setActId(actId)
//        .setForeignId(foreignId)
//        .setForeignType(foreignType)
//        .build()
//    }
//  }
//
//}
