package com.datacloud.datagen.dsp

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.dsp.avro.BidRequestObj
import org.scalacheck.Gen

object BidRequestObjProducer extends App {
  val topicName = "BID_REQUEST"
//  val bootstrapServers = "localhost:9092"
//  val schemaRegistryUrl = "http://localhost:8081"

  val bootstrapServers = "10.12.0.129:9092"
  val schemaRegistryUrl = "http://10.12.0.129:8087"

  val producer = new BidRequestObjProducer(topicName, bootstrapServers, schemaRegistryUrl, 1000L, 10)
  producer.run()
}

class BidRequestObjProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[BidRequestObj](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[BidRequestObj] = genBidRequestObj

  def getKey(t: BidRequestObj) = s"${t.getId}_${t.getImp.getTagid}_${t.getTs}"
}
