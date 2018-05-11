package com.datacloud.report.dataflow.market

import java.time.Instant
import org.scalacheck.Gen


object MarketDecisionResultGenerator {

  def genMarketContentTemplateId: Gen[String] = Gen.oneOf(Seq("7521", "7522", "7523", "7524", "7525"))

  def genMarketWay: Gen[String] = Gen.oneOf(Seq("sms", "email"))

  def genMarketDecisionResult: Gen[MarketDecisionResult] = for {
    tenantId <- Gen.oneOf(Seq("8", "29", "1011", "1012", "1013", "1014", "1015"))
    marketProcessId <- Gen.uuid.map(_.toString)
    bizIdMap <- genBizId
    marketActivityId <- Gen.oneOf(bizIdMap.keys.toSeq)
    decisionStrategyId <- Gen.oneOf(bizIdMap.get(marketActivityId).get)
    marketContentTemplateId <- genMarketContentTemplateId
    personalInfo <- genPersonalInfo
    cardOwnerIndivID = personalInfo.certNo
    cardOwnerName = personalInfo.name
    cardOwnerPhone = personalInfo.phone
    cardOwnerEmail = personalInfo.email
    creditCardNo = personalInfo.creditCardNo
    marketWay <- genMarketWay
    transactionTime <- genTransactionTime
    marketDecisionStrategy <- genMarketDecisionStrategy
  } yield {
    MarketDecisionResult(tenantId, marketProcessId, marketActivityId, decisionStrategyId, marketContentTemplateId, creditCardNo,
      cardOwnerIndivID, cardOwnerName, cardOwnerPhone, cardOwnerEmail, marketWay, transactionTime, marketDecisionStrategy)
  }

}
