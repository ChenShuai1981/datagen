package com.datacloud.report.dataflow.creditinvocationhistory

import java.util

import com.datacloud.polaris.protocol.avro.{CreditDetail, CreditInvocationHistory, Decision, RateType}
import org.scalacheck.Gen


object CreditInvocationHistoryGenerator {

  def genCreditInvocationHistory: Gen[CreditInvocationHistory] = for {
    riskProcessId <- Gen.choose(1234560000L, 1234569999L)
    executionId <- Gen.uuid.map(uuid => Math.abs(uuid.getMostSignificantBits + uuid.getLeastSignificantBits))
    creditStrategyId <- Gen.oneOf(1234567890L to 1234567899L)
    tenantId <- Gen.oneOf(Seq(8L, 29L))
    userId <- Gen.const(100L)
    productCode <- Gen.oneOf("test", "test2", "test3")
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
    creditInvocationHistory.setRiskProcessId(riskProcessId)
    creditInvocationHistory.setExecutionId(executionId)
    creditInvocationHistory.setCreditStrategyId(creditStrategyId)
    creditInvocationHistory.setTenantId(tenantId)
    creditInvocationHistory.setUserId(userId)
    creditInvocationHistory.setProductCode(productCode)
    creditInvocationHistory.setTerminal(terminal)
    creditInvocationHistory.setEventCode(eventCode)
    creditInvocationHistory.setOccurTime(occurTime)
    creditInvocationHistory.setCreditScore(creditScore)
    creditInvocationHistory.setCreditScoreDup(creditScore)
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

  def genDecision: Gen[Decision] = Gen.oneOf(Decision.accept, Decision.reject, Decision.review)

  def genRateType: Gen[Option[RateType]] = Gen.option(Gen.oneOf(RateType.absolute, RateType.relative, RateType.multiple))

}
