package com.datacloud.datagen.feedback

import java.time.{Instant, LocalDateTime}

import com.datacloud.datagen.{AvroDataProducer, KafkaEnv}
import com.datacloud.polaris.protocol.avro.RepaymentEvent
import org.scalacheck.Gen

object RepaymentEventProducer extends App with KafkaEnv {
  val topicName = envPrefix + "REPAYMENT_EVENT"
  val producer = new RepaymentEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 1000L, 10)
  producer.run()
}

class RepaymentEventProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[RepaymentEvent](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[RepaymentEvent] = for {
    riskProcessId <- genRiskProcessId
    terminal <- genTerminal
    productCode <- genProductCode
    tenantId <- genTenantId
    region <- genRegion
    repaymentNo <- Gen.choose(1, 12)
    repaymentAmount <- Gen.choose(1000d, 10000d)
    eventTime <- Gen.const(System.currentTimeMillis()-3600*1000*24*12)
    capital <- Gen.choose(1000.00, 5000.00)
    interest <- Gen.choose(0.10, 0.50)
    penalty <- Gen.choose(1000.00, 50000.00)
    surchargeFee <- Gen.choose(100.00, 3000.00)
    overdueDays <- Gen.choose(0, 5)
    personalInfo <- genPersonalInfo
  } yield {
    val repaymentEvent = new RepaymentEvent()
    repaymentEvent.setCertNo(personalInfo.certNo)
    repaymentEvent.setName(personalInfo.name)
    repaymentEvent.setPhone(personalInfo.phone)
    repaymentEvent.setPhoneCleaned(personalInfo.phone)
    repaymentEvent.setEventTime(eventTime)
    repaymentEvent.setProductCode(productCode)
    repaymentEvent.setRiskProcessId(riskProcessId)
    repaymentEvent.setTenantId(tenantId)
    repaymentEvent.setRegion(region)
    repaymentEvent.setTerminal(terminal)
    repaymentEvent.setRepaymentNo(repaymentNo)
    repaymentEvent.setRepayAmount(repaymentAmount)
    repaymentEvent.setCapital(capital)
    repaymentEvent.setInterest(interest)
    repaymentEvent.setPenalty(penalty)
    repaymentEvent.setSurchargeFee(surchargeFee)
    val ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(eventTime), zoneId)
    val dueDate = ldt.minusDays(overdueDays).toLocalDate.atStartOfDay().atZone(zoneId).toInstant.toEpochMilli
    repaymentEvent.setDueDate(dueDate)
    repaymentEvent
  }

  override def getKey(t: RepaymentEvent): String = s"${t.getRiskProcessId}_${t.getRepaymentNo}"
}