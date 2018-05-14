package com.datacloud.report.dataflow.market

import com.datacloud.report.dataflow.JsonDataProducer
import org.scalacheck.Gen

object UnionPayTrxProducer extends JsonDataProducer[UnionPayTrx] {
  override def getTopic = "drive-ds-UnionPayTrxTopic3"

  override def getBootstrap = "localhost:9092"

  override def getInterval = 60

  override def getLoop: Int = 100000000

  override def genData = for {
    trxAccount <- Gen.identifier
    trxAmount <- Gen.choose(10.0, 1000.0).map(BigDecimal(_))
    trxDescription <- Gen.const("transaction description")
    trxType <- Gen.oneOf(Seq("CONSUME", "REFUND"))
    trxTime <- Gen.const(System.currentTimeMillis())
  } yield UnionPayTrx(trxAccount, trxAmount, trxDescription, trxType, trxTime)

}
