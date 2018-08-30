package com.datacloud.datagen.history

import java.text.SimpleDateFormat

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.polaris.protocol.avro._
import org.scalacheck.Gen

import scala.collection.JavaConversions._

object RiskInvocationHistoryProducer extends App {
  val topicName = "dev_RISK_INVOCATION_HISTORY"

  val bootstrapServers = "10.12.0.131:9092"
  val schemaRegistryUrl = "http://10.12.0.131:8081"

//  val bootstrapServers = "localhost:9092"
//  val schemaRegistryUrl = "http://localhost:8081"

//  val topicName = "preprod_RISK_INVOCATION_HISTORY"
//  val bootstrapServers = "10.12.0.6:9092"
//  val schemaRegistryUrl = "http://10.12.0.6:8081"

  val producer = new RiskInvocationHistoryProducer(topicName, bootstrapServers, schemaRegistryUrl, 60L, 1)
  producer.run()
}

class RiskInvocationHistoryProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
    extends AvroDataProducer[RiskInvocationHistory](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def genOccurTime: Gen[Long] = for {
    dateString <- Gen.choose(21, 22).map(day => if (day < 10) "0"+day.toString else day.toString)
    hourString <- Gen.choose(14, 20).map(hour => if (hour < 10) "0"+hour.toString else hour.toString)
  } yield {
    val ds = "2018-06-" + dateString + " " + hourString + ":00:00"
//    println(ds)
    sdf.parse(ds).getTime
  }

  def genData: Gen[RiskInvocationHistory] = {
    for {
      riskProcessId <- Gen.choose(1234560000L, 1234569999L)
      executionId <- Gen.uuid.map(uuid => Math.abs(uuid.getMostSignificantBits + uuid.getLeastSignificantBits))
      creditStrategyId <- Gen.oneOf(1234567890L to 1234567899L)
      tenantId <- Gen.oneOf(Seq(2557L))
      userId <- Gen.const(100L)
//      productCode <- Gen.oneOf("test", "CAR")
//      terminal <- Gen.oneOf("GENERAL", "WEB", "IOS", "ANDROID")
//      eventCode <- Gen.oneOf("loan", "activation", "loanApply")
      productCode <- Gen.const("test")
      terminal <- Gen.const("GENERAL")
      eventCode <- Gen.const("loanApply")
      occurTime <- genOccurTime // Gen.const(System.currentTimeMillis()) //Gen.choose(1000, 60*1000).map(lag => System.currentTimeMillis() - lag)
      decision <- genDecision
      creditScore <- Gen.choose(300.0, 900.0)
      fraudScore <- Gen.choose(0.0, 100.0)
      name <- Gen.option(Gen.identifier)
      certNo <- genCertNo
      phone <- genPhone
      admissionDetail <- genAdmissionDetail
      antifraudDetail <- genAntifraudDetail
      productId <- Gen.choose(1000L, 10000L)
      eventId <- Gen.choose(1000L, 10000L)
      eventName <- Gen.oneOf("loan", "credit", "apply")
      strategySetId <- Gen.choose(1000L, 10000L)
      strategySetName <- Gen.identifier
      creditDetail <- genCreditDetail
      input <- genInput
      output = input
    } yield {
      val riskInvocationHistory = new RiskInvocationHistory()
      riskInvocationHistory.setAdmissionDetail(admissionDetail)
      riskInvocationHistory.setAntifraudDetail(antifraudDetail)
      riskInvocationHistory.setProductCode(productCode)
      riskInvocationHistory.setProductId(productId)
      riskInvocationHistory.setCertNo(certNo)
      riskInvocationHistory.setCreditDetail(creditDetail)
      riskInvocationHistory.setCreditStrategyRCId(creditStrategyId)
      riskInvocationHistory.setCreditScore(creditScore)
      riskInvocationHistory.setInput(input)
      riskInvocationHistory.setOutput(output)
      riskInvocationHistory.setDecision(decision)
      riskInvocationHistory.setEventCode(eventCode)
      riskInvocationHistory.setEventId(eventId)
      riskInvocationHistory.setEventName(eventName)
      riskInvocationHistory.setExecutionId(executionId)
      riskInvocationHistory.setFraudScore(fraudScore)
      if (name.isDefined) {
        riskInvocationHistory.setName(name.get)
      }
      riskInvocationHistory.setOccurTime(occurTime)
      if (phone.isDefined) {
        riskInvocationHistory.setPhone(phone.get)
      }
      riskInvocationHistory.setProductCode(productCode)
      riskInvocationHistory.setRiskProcessId(riskProcessId)
      riskInvocationHistory.setStrategySetId(strategySetId)
      riskInvocationHistory.setStrategySetName(strategySetName)
      riskInvocationHistory.setTenantId(tenantId)
      riskInvocationHistory.setTerminal(terminal)
      riskInvocationHistory.setUserId(userId)

      riskInvocationHistory
    }
  }

  override def getKey(t: RiskInvocationHistory): String = s"${t.getRiskProcessId}_${t.getOccurTime}"
}
