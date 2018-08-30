package com.datacloud.datagen.history

import java.util

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.polaris.protocol.avro._
import org.scalacheck.Gen

object CreditInvocationHistoryProducer extends App {
  val topicName = "dev_CREDIT_INVOCATION_HISTORY"
  val bootstrapServers = "10.12.0.131:9092"
  val schemaRegistryUrl = "http://10.12.0.131:8081"

//  val topicName = "preprod_CREDIT_INVOCATION_HISTORY"
//  val bootstrapServers = "10.12.0.6:9092"
//  val schemaRegistryUrl = "http://10.12.0.6:8081"

  val producer = new CreditInvocationHistoryProducer(topicName, bootstrapServers, schemaRegistryUrl, 100L, 10)
  producer.run()
}

class CreditInvocationHistoryProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[CreditInvocationHistory](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[CreditInvocationHistory] = {
    for {
      riskProcessId <- Gen.choose(2634567100L, 2934567123L)
      executionId <- Gen.uuid.map(uuid => Math.abs(uuid.getMostSignificantBits + uuid.getLeastSignificantBits))
      creditStrategyId <- Gen.choose(1000L, 1009L)//Gen.choose(100L, 300L)
      tenantId <- Gen.oneOf(Seq(8L))
      userId <- Gen.const(100L)
      productCode <- Gen.oneOf(Seq("test"))
      terminal <- Gen.oneOf("GENERAL", "WEB", "IOS", "ANDROID")
      eventCode <- Gen.oneOf("loan", "activation", "loanApply")
      occurTime <- Gen.choose(1000, 10*60*1000).map(lag => System.currentTimeMillis() - lag)
      decision <- genDecision
      creditScore <- Gen.choose(300.0, 900.0)
      name <- Gen.option(Gen.identifier)
      certNo <- Gen.option(Gen.oneOf("382381236723123", "421312398123123", "2138123721837"))
      phone <- Gen.option(Gen.oneOf("13213927213", "13301293124", "15384238423"))
      amount <- Gen.option(Gen.choose(100d, 1000d))
      rateValue <- Gen.option(Gen.choose(0.3, 0.9))
      compoundPeriod <- Gen.option(Gen.choose(3, 36))
      rateType <- genRateType
    } yield {
      val creditInvocationHistory = new CreditInvocationHistory
//      creditInvocationHistory.setRiskProcessId(riskProcessId)
      creditInvocationHistory.setExecutionId(executionId)
      creditInvocationHistory.setCreditStrategyId(creditStrategyId)
      creditInvocationHistory.setTenantId(tenantId)
      creditInvocationHistory.setUserId(userId)
      creditInvocationHistory.setProductCode(productCode)
      creditInvocationHistory.setTerminal(terminal)
      creditInvocationHistory.setEventCode(eventCode)
      creditInvocationHistory.setOccurTime(occurTime)
      creditInvocationHistory.setCreditScore(creditScore)
      val inputRiskData = new util.HashMap[String, String]()
      if (name.isDefined) {
        inputRiskData.put("name", name.get)
      }
      if (certNo.isDefined) {
        inputRiskData.put("certNo", certNo.get)
      }
      if (phone.isDefined) {
        inputRiskData.put("phone", phone.get)
      }
      creditInvocationHistory.setInput(inputRiskData)

      creditInvocationHistory.setOutput(new util.HashMap())

      val creditDetail = new CreditDetail()
      creditDetail.setCreditDecision(Decision.valueOf(decision.name()))
      if (amount.isDefined) {
        creditDetail.setAmount(amount.get)
      }
      if (compoundPeriod.isDefined) {
        creditDetail.setCompoundPeriod(compoundPeriod.get)
      }
      if (rateValue.isDefined) {
        creditDetail.setRateValue(rateValue.get)
      }
      if (rateType.isDefined) {
        creditDetail.setRateType(RateType.valueOf(rateType.get.name()))
      }
      creditInvocationHistory.setCreditDetail(creditDetail)

      creditInvocationHistory
    }
  }

  override def getKey(t: CreditInvocationHistory): String = s"${t.getRiskProcessId}_${t.getOccurTime}"
}
