package com.datacloud.report.dataflow.market

import org.scalacheck.Gen

object GainActivityRightEventGenerator {

  def genGainActivityRightEvent: Gen[GainActivityRightEvent] = for {
    tenantId <- Gen.oneOf(Seq("8", "29", "1011", "1012", "1013", "1014", "1015"))
    bizIdMap <- genBizId
    marketActivityId <- Gen.oneOf(bizIdMap.keys.toSeq)
    decisionStrategyId <- Gen.oneOf(bizIdMap.get(marketActivityId).get)
    personalInfo <- genPersonalInfo
    cardOwnerIndivID = personalInfo.certNo
    cardOwnerName = personalInfo.name
    cardOwnerPhone = personalInfo.phone
    cardOwnerEmail = personalInfo.email
    creditCardNo = personalInfo.creditCardNo
    gainActivityRightTime <- genTransactionTime
  } yield {
    GainActivityRightEvent(tenantId, marketActivityId, decisionStrategyId, cardOwnerIndivID, cardOwnerName,
      cardOwnerPhone, cardOwnerEmail, creditCardNo, gainActivityRightTime)
  }
}
