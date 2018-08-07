package com.datacloud.datagen.feedback

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.polaris.protocol.avro.{LoanEndEvent, LoanEndType}
import org.scalacheck.Gen

object LoanEndEventProducer extends App {
  val topicName = "dev_LOANEND_EVENT"
  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"
  val schemaRegistryUrl = "http://ambari-agent4.sit.geerong.com:8081"

//  val topicName = "preprod_LOANEND_EVENT"
//  val bootstrapServers = "10.12.0.6:9092"
//  val schemaRegistryUrl = "http://10.12.0.6:8081"

  val producer = new LoanEndEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 600L, 100)
  producer.run()
}

class LoanEndEventProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[LoanEndEvent](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[LoanEndEvent] = for {
    riskProcessId <- genRiskProcessId
    terminal <- genTerminal
    productCode <- genProductCode
    tenantId <- genTenantId
    eventTime <- Gen.const(System.currentTimeMillis()-3600*1000*24*6)
    loanEndType <- Gen.oneOf(LoanEndType.values())
    personalInfo <- genPersonalInfo
    cancelTerm <- Gen.choose(1, 6)
    planRepayment <- genPlanRepaymentList(eventTime, cancelTerm)
  } yield {
    val loanEndEvent = new LoanEndEvent()
    loanEndEvent.setCertNo(personalInfo.certNo)
    loanEndEvent.setName(personalInfo.name)
    loanEndEvent.setPhone(personalInfo.phone)
    loanEndEvent.setEventTime(eventTime)
    loanEndEvent.setLoanEndType(loanEndType)
    loanEndEvent.setProductCode(productCode)
//    loanEndEvent.setRiskProcessId(riskProcessId)
    loanEndEvent.setRiskProcessId(111111121L)
    loanEndEvent.setTenantId(tenantId)
    loanEndEvent.setTerminal(terminal)
    loanEndEvent.setCancelledPlanRepayment(planRepayment)
    loanEndEvent
  }

  override def getKey(t: LoanEndEvent): String = s"${t.getRiskProcessId}_${t.getEventTime}"
}
