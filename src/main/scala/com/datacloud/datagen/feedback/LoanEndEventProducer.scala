package com.datacloud.datagen.feedback


import com.datacloud.datagen.history.RiskInvocationHistoryProducer.envPrefix
import com.datacloud.datagen.{AvroDataProducer, KafkaEnv}
import com.datacloud.polaris.protocol.avro.{LoanEndEvent, LoanEndType, PaymentPlan, Region}
import org.scalacheck.Gen

object LoanEndEventProducer extends App with KafkaEnv {
  val topicName = envPrefix + "LOANEND_EVENT"
  val producer = new LoanEndEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 1000L, 100)
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
    loanEndEvent.setCertNo("362502198101110611")
    loanEndEvent.setName(personalInfo.name)
    loanEndEvent.setPhone(personalInfo.phone)
    loanEndEvent.setPhone("13801899711")
    loanEndEvent.setPhoneCleaned(personalInfo.phone)
    loanEndEvent.setPhoneCleaned("13801899711")
    loanEndEvent.setEventTime(eventTime)
    loanEndEvent.setLoanEndType(loanEndType)
    loanEndEvent.setProductCode(productCode)
    loanEndEvent.setRiskProcessId(riskProcessId)
    loanEndEvent.setRiskProcessId(2932999780L)
    loanEndEvent.setTenantId(tenantId)
    loanEndEvent.setTenantId(16L)
    loanEndEvent.setRegion(region)
    loanEndEvent.setRegion(Region.INDONESIA)
    loanEndEvent.setTerminal(terminal)
    loanEndEvent.setCancelledPlanRepayment(planRepayment)
//    loanEndEvent.setCancelledPlanRepayment(new util.ArrayList[PaymentPlan]())
    loanEndEvent
  }

  override def getKey(t: LoanEndEvent): String = s"${t.getRiskProcessId}"
}
