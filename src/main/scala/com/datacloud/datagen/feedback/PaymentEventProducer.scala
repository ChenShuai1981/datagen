package com.datacloud.datagen.feedback

import java.time.{Instant, LocalDateTime}

import com.datacloud.datagen.history.RiskInvocationHistoryProducer.envPrefix
import com.datacloud.datagen.{AvroDataProducer, KafkaEnv}
import com.datacloud.polaris.protocol.avro.{PaymentEvent, Region}
import org.scalacheck.Gen

object PaymentEventProducer extends App with KafkaEnv {
  val topicName = envPrefix + "PAYMENT_EVENT"
  val producer = new PaymentEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 1000L, 100)
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
//    paymentEvent.setCertNo("362502198101110611")
    paymentEvent.setName(personalInfo.name)
    paymentEvent.setPhone(personalInfo.phone)
//    paymentEvent.setPhone("13801899711")
    paymentEvent.setPhoneCleaned(personalInfo.phone)
//    paymentEvent.setPhoneCleaned("13801899711")
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
//    paymentEvent.setRiskProcessId(2932999780L)
    paymentEvent.setTenantId(tenantId)
    paymentEvent.setRegion(region)
//    paymentEvent.setRegion(Region.INDONESIA)
    paymentEvent.setTerminal(terminal)
    paymentEvent.setPlanRepayment(planRepayment)
    paymentEvent
  }

  override def getKey(t: PaymentEvent): String = s"${t.getRiskProcessId}"
}
