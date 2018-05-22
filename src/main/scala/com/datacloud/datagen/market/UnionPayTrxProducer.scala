package com.datacloud.datagen.market

import com.datacloud.datagen.JsonDataProducer
import org.scalacheck.Gen

object UnionPayTrxProducer extends App {
  val topicName = "dev_drive-ds-UnionPayTrxTopic"
  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"
  val interval = 60L
  val loop = 10

  val producer = new GainActivityRightEventProducer(topicName, bootstrapServers, interval, loop)
  producer.run()
}

class UnionPayTrxProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[UnionPayTrx](topicName, bootstrapServers, interval, loop) {

  override def genData = for {
    trxAccount <- Gen.identifier
    trxAmount <- Gen.choose(10.0, 1000.0).map(BigDecimal(_))
    trxDescription <- Gen.const("transaction description")
    trxType <- Gen.oneOf(Seq("CONSUME", "REFUND"))
    trxTime <- Gen.const(System.currentTimeMillis())
  } yield UnionPayTrx(trxAccount, trxAmount, trxDescription, trxType, trxTime)

}
