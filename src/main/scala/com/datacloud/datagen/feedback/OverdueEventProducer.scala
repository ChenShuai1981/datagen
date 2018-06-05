package com.datacloud.datagen.feedback

import java.time.{Instant, LocalDateTime}

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.polaris.protocol.avro.OverdueEvent
import org.scalacheck.Gen

object OverdueEventProducer extends App {
//  val topicName = "sit_OVERDUE_EVENT"
//  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"
//  val schemaRegistryUrl = "http://ambari-agent4.sit.geerong.com:8081"

  val topicName = "preprod_OVERDUE_EVENT"
  val bootstrapServers = "10.12.0.6:9092"
  val schemaRegistryUrl = "http://10.12.0.6:8081"

  val producer = new OverdueEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 60L, 1)
  producer.run()
}

class OverdueEventProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
    extends AvroDataProducer[OverdueEvent](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[OverdueEvent] = for {
    riskProcessId <- genRiskProcessId
    terminal <- genTerminal
    productCode <- genProductCode
    tenantId <- genTenantId
    overdueAmount <- Gen.choose(1000d, 10000d)
    overdueDays <- Gen.choose(1, 3)
    eventTime <- Gen.const(System.currentTimeMillis())
    personalInfo <- genPersonalInfo
  } yield {
    val overdueEvent = new OverdueEvent()
    overdueEvent.setCertNo(personalInfo.certNo)
    overdueEvent.setName(personalInfo.name)
    overdueEvent.setPhone(personalInfo.phone)
    val ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(eventTime), zoneId)
//    val dueDate = ldt.minusDays(overdueDays).toLocalDate.atStartOfDay().atZone(zoneId).toInstant.toEpochMilli
    val dueDate = ldt.toLocalDate.atStartOfDay().atZone(zoneId).toInstant.toEpochMilli
    overdueEvent.setDueDate(dueDate)
    overdueEvent.setOverdueStartDate(dueDate+3600*1000*24*1)
    overdueEvent.setEventTime(eventTime)
    overdueEvent.setOverdueAmount(overdueAmount)
    overdueEvent.setOverdueDays(overdueDays)
    overdueEvent.setProductCode(productCode)
    overdueEvent.setRiskProcessId(riskProcessId)
//    overdueEvent.setRiskProcessId(1234566568L)
    overdueEvent.setTenantId(tenantId)
    overdueEvent.setTerminal(terminal)
    overdueEvent
  }

}
