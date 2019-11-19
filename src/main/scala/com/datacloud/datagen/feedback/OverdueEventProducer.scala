<<<<<<< HEAD
package com.datacloud.datagen.feedback

import java.time.{Instant, LocalDateTime}

import com.datacloud.datagen.history.RiskInvocationHistoryProducer.envPrefix
import com.datacloud.datagen.{AvroDataProducer, KafkaEnv}
import com.datacloud.polaris.protocol.avro.{OverdueEvent, Region}
import org.scalacheck.Gen

object OverdueEventProducer extends App with KafkaEnv {
  val topicName = envPrefix + "OVERDUE_EVENT"
  val producer = new OverdueEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 1000L, 1)
  producer.run()
}

class OverdueEventProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
    extends AvroDataProducer[OverdueEvent](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[OverdueEvent] = for {
    riskProcessId <- genRiskProcessId
    terminal <- genTerminal
    productCode <- genProductCode
    tenantId <- genTenantId
    region <- genRegion
    overdueAmount <- Gen.choose(1000d, 10000d)
    overdueDays <- Gen.choose(6, 8)
    eventTime <- Gen.const(System.currentTimeMillis())
    personalInfo <- genPersonalInfo
    overdueNo <- Gen.choose(1, 12)
//    overdueStartDate <- Gen.const(1528646400000L)
//    dueDate <- Gen.const(1528560000000L)
  } yield {
    val overdueEvent = new OverdueEvent()
    overdueEvent.setCertNo(personalInfo.certNo)
    overdueEvent.setCertNo("362502198101110601")
    overdueEvent.setName(personalInfo.name)
    overdueEvent.setName("张三")
    overdueEvent.setPhone(personalInfo.phone)
    overdueEvent.setPhone("142345733333")
    overdueEvent.setPhoneCleaned(personalInfo.phone)
    overdueEvent.setPhoneCleaned("142345733333")
    val ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(eventTime), zoneId)
    val dueDate = ldt.minusDays(overdueDays).toLocalDate.atStartOfDay().atZone(zoneId).toInstant.toEpochMilli
//    var dueDate = 1546666626110L
    overdueEvent.setDueDate(dueDate)
    overdueEvent.setOverdueStartDate(dueDate+3600*1000*24*1)
    overdueEvent.setEventTime(eventTime)
//    overdueEvent.setEventTime(1546617626110L)
    overdueEvent.setOverdueAmount(overdueAmount)
    overdueEvent.setOverdueAmount(1000.00)
    overdueEvent.setOverdueDays(overdueDays)
    overdueEvent.setOverdueDays(8)
    overdueEvent.setProductCode(productCode)
    overdueEvent.setProductCode("CCC")
    overdueEvent.setRiskProcessId(riskProcessId)
    overdueEvent.setRiskProcessId(218361826L)
    overdueEvent.setTenantId(tenantId)
    overdueEvent.setTenantId(908L)
    overdueEvent.setRegion(region)
    overdueEvent.setRegion(Region.VIETNAM)
    overdueEvent.setTerminal(terminal)
    overdueEvent.setTerminal("GENERAL")
    overdueEvent.setOverdueNo(overdueNo)
    overdueEvent.setOverdueNo(1)

    overdueEvent
  }

  override def getKey(t: OverdueEvent): String = s"${t.getRiskProcessId}_${t.getOverdueNo}"
}
=======
//package com.datacloud.datagen.feedback
//
//import java.time.{Instant, LocalDateTime}
//
//import com.datacloud.datagen.AvroDataProducer
//import com.datacloud.polaris.protocol.avro.OverdueEvent
//import org.scalacheck.Gen
//
//object OverdueEventProducer extends App {
////  val topicName = "sit_OVERDUE_EVENT"
////  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"
////  val schemaRegistryUrl = "http://ambari-agent4.sit.geerong.com:8081"
//
//  val topicName = "preprod_OVERDUE_EVENT"
//  val bootstrapServers = "10.12.0.6:9092"
//  val schemaRegistryUrl = "http://10.12.0.6:8081"
//
//  val producer = new OverdueEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 60L, 1)
//  producer.run()
//}
//
//class OverdueEventProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
//    extends AvroDataProducer[OverdueEvent](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {
//
//  def genData: Gen[OverdueEvent] = for {
//    riskProcessId <- genRiskProcessId
//    terminal <- genTerminal
//    productCode <- genProductCode
//    tenantId <- genTenantId
//    overdueAmount <- Gen.choose(1000d, 10000d)
//    overdueDays <- Gen.choose(1, 3)
//    eventTime <- Gen.const(System.currentTimeMillis())
//    personalInfo <- genPersonalInfo
//  } yield {
//    val overdueEvent = new OverdueEvent()
//    overdueEvent.setCertNo(personalInfo.certNo)
//    overdueEvent.setName(personalInfo.name)
//    overdueEvent.setPhone(personalInfo.phone)
//    val ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(eventTime), zoneId)
////    val dueDate = ldt.minusDays(overdueDays).toLocalDate.atStartOfDay().atZone(zoneId).toInstant.toEpochMilli
//    val dueDate = ldt.toLocalDate.atStartOfDay().atZone(zoneId).toInstant.toEpochMilli
//    overdueEvent.setDueDate(dueDate)
//    overdueEvent.setOverdueStartDate(dueDate+3600*1000*24*1)
//    overdueEvent.setEventTime(eventTime)
//    overdueEvent.setOverdueAmount(overdueAmount)
//    overdueEvent.setOverdueDays(overdueDays)
//    overdueEvent.setProductCode(productCode)
//    overdueEvent.setRiskProcessId(riskProcessId)
////    overdueEvent.setRiskProcessId(1234566568L)
//    overdueEvent.setTenantId(tenantId)
//    overdueEvent.setTerminal(terminal)
//    overdueEvent
//  }
//
//}
>>>>>>> ad393f7091de855dec8c28b578de64be41f01242
