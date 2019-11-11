//package com.datacloud.datagen.feedback
//
//import java.time.{Instant, LocalDateTime}
//
//import com.datacloud.datagen.AvroDataProducer
//import com.datacloud.polaris.protocol.avro.PaymentEvent
//import org.scalacheck.Gen
//
//object PaymentEventProducer extends App {
////  val topicName = "dev_PAYMENT_EVENT"
////  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"
////  val schemaRegistryUrl = "http://ambari-agent4.sit.geerong.com:8081"
//
//  val topicName = "preprod_PAYMENT_EVENT"
//  val bootstrapServers = "10.12.0.6:9092"
//  val schemaRegistryUrl = "http://10.12.0.6:8081"
//
//  val producer = new PaymentEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 60L, 1)
//  producer.run()
//}
//
//class PaymentEventProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
//  extends AvroDataProducer[PaymentEvent](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {
//
//  def genData: Gen[PaymentEvent] = for {
//    riskProcessId <- genRiskProcessId
//    terminal <- genTerminal
//    productCode <- genProductCode
//    tenantId <- genTenantId
//    principalAmount <- Gen.choose(1000d, 10000d)
//    eventTime <- Gen.const(System.currentTimeMillis()-3600*1000*24*12)
//    loanTerm <- Gen.choose(12, 18)
//    loanRate <- Gen.choose(0.10, 0.50)
//    planRepayment <- genPlanRepaymentList(eventTime, loanTerm)
//    (penaltyFixFee, penaltyInterestRate) <- genPenalty
//    (surchargeFixFee, surchargeRate) <- genSurcharge
//    personalInfo <- genPersonalInfo
//  } yield {
//    val paymentEvent = new PaymentEvent()
//    paymentEvent.setCertNo(personalInfo.certNo)
//    paymentEvent.setName(personalInfo.name)
//    paymentEvent.setPhone(personalInfo.phone)
//    val ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(eventTime), zoneId).plusDays(1)
//    val valueDate = ldt.atZone(zoneId).toInstant.toEpochMilli
//    paymentEvent.setValueDate(valueDate)
//    paymentEvent.setEventTime(eventTime)
//    paymentEvent.setPrincipalAmount(principalAmount)
//    paymentEvent.setLoanTerm(loanTerm)
//    paymentEvent.setLoanRate(loanRate)
//    if (penaltyInterestRate.isDefined) {
//      paymentEvent.setPenaltyInterestRate(penaltyInterestRate.get)
//    }
//    if (penaltyFixFee.isDefined) {
//      paymentEvent.setPenaltyFixFee(penaltyFixFee.get)
//    }
//    if (surchargeRate.isDefined) {
//      paymentEvent.setSurchargeRate(surchargeRate.get)
//    }
//    if (surchargeFixFee.isDefined) {
//      paymentEvent.setSurchargeFixFee(surchargeFixFee.get)
//    }
//    paymentEvent.setProductCode(productCode)
//    paymentEvent.setRiskProcessId(riskProcessId)
//    paymentEvent.setTenantId(tenantId)
//    paymentEvent.setTerminal(terminal)
//    paymentEvent.setPlanRepayment(planRepayment)
//    paymentEvent
//  }
//
//}
