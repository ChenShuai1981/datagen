package com.datacloud.datagen.market

import com.datacloud.datagen.JsonDataProducer
import org.scalacheck.Gen

object GainActivityRightEventProducer extends App {
  val topicName = "dev_GAIN_ACTIVITY_RIGHT"
  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"
//  val bootstrapServers = "localhost:9092"
  val interval = 60L
  val loop = 1

  val producer = new GainActivityRightEventProducer(topicName, bootstrapServers, interval, loop)
  producer.run()
}

class GainActivityRightEventProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[GainActivityRightEvent](topicName, bootstrapServers, interval, loop) {

  override def genData = for {
    tenantId <- Gen.oneOf(Seq("2621"))
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
    val event = GainActivityRightEvent(tenantId, marketActivityId, decisionStrategyId, cardOwnerIndivID, cardOwnerName,
      cardOwnerPhone, cardOwnerEmail, creditCardNo, gainActivityRightTime, true)
    event.copy(tenantId = "2621", marketActivityId = "516", cardOwnerIndivID = "310101198001010003", creditCardNo = "4000123456780003")
  }

}