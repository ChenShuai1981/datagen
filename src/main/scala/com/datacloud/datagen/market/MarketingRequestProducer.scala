package com.datacloud.datagen.market

import com.datacloud.datagen.JsonDataProducer
import org.scalacheck.Gen

object MarketingRequestProducer extends App {
  val topicName = "dev_MARKETING_REQUEST"
  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"

  val producer = new MarketingRequestProducer(topicName, bootstrapServers, 600L, 10)
  producer.run()
}

class MarketingRequestProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[MarketingRequest](topicName, bootstrapServers, interval, loop) {

  override def genData = {
    for {
      tenantId <- Gen.oneOf(Seq(2621))
      marketActivityId <- Gen.oneOf(Seq(28321, 21983))
      decisionStrategyId <- Gen.oneOf(Seq(28321, 21983))
      strategyDeployId <- Gen.oneOf(Seq(28321, 21983))
      occurTime <- Gen.const(System.currentTimeMillis())
      extraData <- genExtraData
    } yield {
      MarketingRequest(tenantId, marketActivityId, decisionStrategyId, strategyDeployId, occurTime, extraData)
    }
  }

  override def getKey(t: MarketingRequest): String = t.occurTime.toString
}
