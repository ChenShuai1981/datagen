package com.datacloud.datagen.market

import com.datacloud.datagen.JsonDataProducer
import org.scalacheck.Gen

object MarketDecisionResultProducer extends App {
  val topicName = "dev_MARKET_DECISION_RESULT"
  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"
// val bootstrapServers = "localhost:9092"
  val interval = 60L
  val loop = 1

  val producer = new MarketDecisionResultProducer(topicName, bootstrapServers, interval, loop)
  producer.run()
}

class MarketDecisionResultProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
    extends JsonDataProducer[MarketDecisionResult](topicName, bootstrapServers, interval, loop) {

    override def genData = {
      def genStrategyDeployId: Gen[String] = Gen.oneOf(Seq("7521", "7522", "7523", "7524", "7525"))

      def genAppendix: Gen[Appendix] = for {
        gzcbCreditCardType <- Gen.oneOf(Seq("标准信用卡"))
        tenantId <- Gen.choose(2000, 4000)
        transactionType <- Gen.const("消费")
        transactionContext <- Gen.const("支付宝快捷")
        executionId <- Gen.choose(362584182758572035L, 362684182758572035L)
        rejectReason <- Gen.const("支付方式不符")
        transactionAmount <- Gen.choose(100, 10000).map(_.toString)
      } yield Appendix(gzcbCreditCardType, tenantId, transactionType, transactionContext, executionId, rejectReason, transactionAmount)

      def genMarketWay: Gen[String] = Gen.oneOf(Seq("sms", "email"))

      for {
        tenantId <- Gen.oneOf(Seq("2621"))
        marketProcessId <- Gen.uuid.map(_.toString)
        bizIdMap <- genBizId
        marketActivityId <- Gen.oneOf(bizIdMap.keys.toSeq)
        decisionStrategyId <- Gen.oneOf(bizIdMap.get(marketActivityId).get)
        strategyDeployId <- genStrategyDeployId
        personalInfo <- genPersonalInfo
        cardOwnerIndivID = personalInfo.certNo
        cardOwnerName = personalInfo.name
        cardOwnerPhone = personalInfo.phone
        cardOwnerEmail = personalInfo.email
        creditCardNo = personalInfo.creditCardNo
        marketWay <- genMarketWay
        transactionTime <- genTransactionTime
        marketDecisionStrategy <- genMarketDecisionStrategy
        appendix <- genAppendix
      } yield {
        MarketDecisionResult(tenantId, marketProcessId, marketActivityId, decisionStrategyId, strategyDeployId, creditCardNo,
          cardOwnerIndivID, cardOwnerName, cardOwnerPhone, cardOwnerEmail, marketWay, transactionTime, marketDecisionStrategy, appendix)
      }
    }
}
