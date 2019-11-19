package com.datacloud.datagen.history

import java.text.SimpleDateFormat

import com.datacloud.datagen.{AvroDataProducer, KafkaEnv}
import com.datacloud.polaris.protocol.avro._
import org.scalacheck.Gen

import scala.collection.JavaConversions._

object RiskInvocationHistoryProducer extends App with KafkaEnv {
  val topicName = envPrefix + "RISK_INVOCATION_HISTORY"
  val producer = new RiskInvocationHistoryProducer(topicName, bootstrapServers, schemaRegistryUrl, 600L, 1)
  producer.run()
}

class RiskInvocationHistoryProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
    extends AvroDataProducer[RiskInvocationHistory](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def genOccurTime: Gen[Long] = for {
    dateString <- Gen.choose(21, 22).map(day => if (day < 10) "0"+day.toString else day.toString)
    hourString <- Gen.choose(14, 20).map(hour => if (hour < 10) "0"+hour.toString else hour.toString)
  } yield {
    val ds = "2018-09-" + dateString + " " + hourString + ":00:00"
//    println(ds)
    sdf.parse(ds).getTime
  }

  def genData: Gen[RiskInvocationHistory] = {
    for {
      riskProcessId <- Gen.choose(2534560000L, 2834569999L)
      executionId <- Gen.uuid.map(uuid => Math.abs(uuid.getMostSignificantBits + uuid.getLeastSignificantBits))
      creditStrategyId <- Gen.oneOf(1234567890L to 1234567899L)

      tenantId <- Gen.oneOf(Seq(8L))
      region <- genRegion

      name <- Gen.oneOf(Seq("小二", "老大", "张三", "李四", "王五"))
      certNo <- genCertNo
      phone <- genPhone

      eventId <- Gen.choose(1000L, 10000L)
      eventName <- genEventCode
      eventCode <- genEventCode

      productId <- Gen.choose(1000L, 10000L)
      productCode <- genProductCode

      userId <- Gen.const(100L) // poseidon user
      terminal <- genTerminal
      occurTime <- Gen.const(System.currentTimeMillis()) //genOccurTime Gen.choose(1000, 60*1000).map(lag => System.currentTimeMillis() - lag)
      decision <- genDecision
      creditScore <- Gen.choose(300.0, 900.0)
      fraudScore <- Gen.choose(0.0, 100.0)
      executedStrategyPhases <- Gen.someOf(Seq("admission", "antifraud", "credit"))
      admissionDetail <- genAdmissionDetail
      antifraudDetail <- genAntifraudDetail
      strategySetId <- Gen.choose(1000L, 10000L)
      strategySetName <- Gen.identifier
      creditDetail <- genCreditDetail
      input <- genInput(certNo, name, phone)
      output = input
      orderNo <- Gen.identifier
    } yield {
      val riskInvocationHistory = new RiskInvocationHistory()
      riskInvocationHistory.setAdmissionDetail(null)
      riskInvocationHistory.setAntifraudDetail(null)
      riskInvocationHistory.setProductCode(productCode)
      riskInvocationHistory.setProductId(productId)
      riskInvocationHistory.setCertNo(certNo)
      riskInvocationHistory.setCertNo("441826197812242766")
      riskInvocationHistory.setCreditDetail(creditDetail)
      riskInvocationHistory.setAdmissionDetail(admissionDetail)
      riskInvocationHistory.setAntifraudDetail(antifraudDetail)
      riskInvocationHistory.setCreditStrategyRCId(creditStrategyId)
      riskInvocationHistory.setCreditScore(creditScore)
      riskInvocationHistory.setInput(input)
      riskInvocationHistory.setOutput(output)
      riskInvocationHistory.setDecision(decision)
      riskInvocationHistory.setEventCode(eventCode)
      riskInvocationHistory.setEventId(eventId)
      riskInvocationHistory.setEventName(eventName)
      riskInvocationHistory.setExecutedStrategyPhases(executedStrategyPhases)
      riskInvocationHistory.setExecutionId(executionId)
      riskInvocationHistory.setFraudScore(fraudScore)
      riskInvocationHistory.setName(name)
      riskInvocationHistory.setName("右右右")
      riskInvocationHistory.setOccurTime(occurTime)
      riskInvocationHistory.setPhone(phone)
      riskInvocationHistory.setPhone("13801899715")
      riskInvocationHistory.setPhoneCleaned(phone)
      riskInvocationHistory.setPhoneCleaned("13801899715")
      riskInvocationHistory.setProductCode(productCode)
      riskInvocationHistory.setRiskProcessId(riskProcessId)
      riskInvocationHistory.setRiskProcessId(2932999890L)
      riskInvocationHistory.setStrategySetId(strategySetId)
      riskInvocationHistory.setStrategySetName(strategySetName)
      riskInvocationHistory.setTenantId(tenantId)
      riskInvocationHistory.setTenantId(16L)
      riskInvocationHistory.setRegion(region)
      riskInvocationHistory.setRegion(Region.PRC)
      riskInvocationHistory.setTerminal(terminal)
      riskInvocationHistory.setUserId(userId)
      riskInvocationHistory.setOrderNo(orderNo)
      riskInvocationHistory
    }
  }

  override def getKey(t: RiskInvocationHistory): String = s"${t.getRiskProcessId}_${t.getEventId}"
}
