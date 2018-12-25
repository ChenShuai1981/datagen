package com.datacloud.datagen.feedback

import java.time.{Instant, LocalDateTime}

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.polaris.protocol.avro.{PaymentEvent, Region}
import org.scalacheck.Gen

object PaymentEventProducer extends App {

//  val topicName = "loc_PAYMENT_EVENT"
//  val bootstrapServers = "localhost:9092"
//  val schemaRegistryUrl = "http://localhost:8081"

  val topicName = "dev_PAYMENT_EVENT"
  val bootstrapServers = "10.12.0.131:9092"
  val schemaRegistryUrl = "http://10.12.0.131:8081"

//  val topicName = "preprod_PAYMENT_EVENT"
//  val bootstrapServers = "10.12.0.6:9092"
//  val schemaRegistryUrl = "http://10.12.0.6:8081"

//  val topicName = "preprod_PAYMENT_EVENT"
//  val bootstrapServers = "10.12.0.175:9092"
//  val schemaRegistryUrl = "http://10.12.0.175:8081"

  val producer = new PaymentEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 100L, 100)
  producer.run()
}

class PaymentEventProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[PaymentEvent](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[PaymentEvent] = for {
    riskProcessId <- genRiskProcessId
    terminal <- genTerminal
    productCode <- genProductCode
    tenantId <- genTenantId
    region <- genRegion
    principalAmount <- Gen.choose(1000d, 10000d)
    eventTime <- Gen.const(System.currentTimeMillis()-3600*1000*24*12)
    loanTerm <- Gen.choose(12, 18)
    loanRate <- Gen.choose(0.10, 0.50)
    planRepayment <- genPlanRepaymentList(eventTime, loanTerm)
    (penaltyFixFee, penaltyInterestRate) <- genPenalty
    (surchargeFixFee, surchargeRate) <- genSurcharge
    personalInfo <- genPersonalInfo
  } yield {
    val paymentEvent = new PaymentEvent()
    paymentEvent.setCertNo(personalInfo.certNo)
    paymentEvent.setCertNo("362502198101110612")
    paymentEvent.setName(personalInfo.name)
    paymentEvent.setPhone(personalInfo.phone)
    paymentEvent.setPhone("13801899712")
    paymentEvent.setPhoneCleaned(personalInfo.phone)
    paymentEvent.setPhoneCleaned("13801899712")
    val ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(eventTime), zoneId).plusDays(1)
    val valueDate = ldt.atZone(zoneId).toInstant.toEpochMilli
    paymentEvent.setValueDate(valueDate)
    paymentEvent.setEventTime(eventTime)
    paymentEvent.setPrincipalAmount(principalAmount)
    paymentEvent.setLoanTerm(loanTerm)
    paymentEvent.setLoanRate(loanRate)
    if (penaltyInterestRate.isDefined) {
      paymentEvent.setPenaltyInterestRate(penaltyInterestRate.get)
    }
    if (penaltyFixFee.isDefined) {
      paymentEvent.setPenaltyFixFee(penaltyFixFee.get)
    }
    if (surchargeRate.isDefined) {
      paymentEvent.setSurchargeRate(surchargeRate.get)
    }
    if (surchargeFixFee.isDefined) {
      paymentEvent.setSurchargeFixFee(surchargeFixFee.get)
    }
    paymentEvent.setProductCode(productCode)
    paymentEvent.setRiskProcessId(riskProcessId)
    paymentEvent.setRiskProcessId(29382342870L)
    paymentEvent.setTenantId(tenantId)
    paymentEvent.setRegion(region)
    paymentEvent.setRegion(Region.PRC)
    paymentEvent.setTerminal(terminal)
    paymentEvent.setPlanRepayment(planRepayment)
    paymentEvent
  }

  override def getKey(t: PaymentEvent): String = s"${t.getRiskProcessId}"
}
