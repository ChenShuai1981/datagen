package com.datacloud.datagen.feedback


import com.datacloud.datagen.AvroDataProducer
import com.datacloud.polaris.protocol.avro.{LoanEndEvent, LoanEndType, PaymentPlan, Region}
import org.scalacheck.Gen

object LoanEndEventProducer extends App {

//    val topicName = "loc_LOANEND_EVENT"
//    val bootstrapServers = "localhost:9092"
//    val schemaRegistryUrl = "http://localhost:8081"

  val topicName = "dev_LOANEND_EVENT"
  val bootstrapServers = "10.12.0.131:9092"
  val schemaRegistryUrl = "http://10.12.0.131:8081"

//  val topicName = "preprod_LOANEND_EVENT"
//  val bootstrapServers = "10.12.0.6:9092"
//  val schemaRegistryUrl = "http://10.12.0.6:8081"

//  val topicName = "preprod_LOANEND_EVENT"
//  val bootstrapServers = "10.12.0.175:9092"
//  val schemaRegistryUrl = "http://10.12.0.175:8081"

  val producer = new LoanEndEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 100L, 100)
  producer.run()
}

class LoanEndEventProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[LoanEndEvent](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[LoanEndEvent] = for {
    riskProcessId <- genRiskProcessId
    terminal <- genTerminal
    productCode <- genProductCode
    tenantId <- genTenantId
    region <- genRegion
    eventTime <- Gen.const(System.currentTimeMillis()-3600*1000*24*6)
    loanEndType <- Gen.oneOf(LoanEndType.values())
    personalInfo <- genPersonalInfo
    cancelTerm <- Gen.choose(1, 6)
    planRepayment <- genPlanRepaymentList(eventTime, cancelTerm)
  } yield {
    val loanEndEvent = new LoanEndEvent()
    loanEndEvent.setCertNo(personalInfo.certNo)
    loanEndEvent.setCertNo("362502198101110612")
    loanEndEvent.setName(personalInfo.name)
    loanEndEvent.setPhone(personalInfo.phone)
    loanEndEvent.setPhone("13801899712")
    loanEndEvent.setPhoneCleaned(personalInfo.phone)
    loanEndEvent.setPhoneCleaned("13801899712")
    loanEndEvent.setEventTime(eventTime)
    loanEndEvent.setLoanEndType(loanEndType)
    loanEndEvent.setProductCode(productCode)
    loanEndEvent.setRiskProcessId(riskProcessId)
    loanEndEvent.setRiskProcessId(29382342870L)
    loanEndEvent.setTenantId(tenantId)
    loanEndEvent.setRegion(region)
    loanEndEvent.setRegion(Region.PRC)
    loanEndEvent.setTerminal(terminal)
    loanEndEvent.setCancelledPlanRepayment(planRepayment)
//    loanEndEvent.setCancelledPlanRepayment(new util.ArrayList[PaymentPlan]())
    loanEndEvent
  }

  override def getKey(t: LoanEndEvent): String = s"${t.getRiskProcessId}"
}
