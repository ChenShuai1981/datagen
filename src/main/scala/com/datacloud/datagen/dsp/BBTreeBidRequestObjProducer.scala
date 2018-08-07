package com.datacloud.datagen.dsp

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.dsp.avro.bbtree.BBTreeBidRequestObj
import org.scalacheck.Gen

object BBTreeBidRequestObjProducer extends App {
  val topicName = "loc_BBTREE_BID_REQUEST"
  val bootstrapServers = "localhost:9092"
  val schemaRegistryUrl = "http://localhost:8081"

  val producer = new BBTreeBidRequestObjProducer(topicName, bootstrapServers, schemaRegistryUrl, 600L, 10)
  producer.run()
}

class BBTreeBidRequestObjProducer (topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[BBTreeBidRequestObj](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[BBTreeBidRequestObj] = genBBTreeBidRequestObj

  def getKey(t: BBTreeBidRequestObj) = s"${t.getId}_${t.getImp.getTagid}_${t.getTs}"
}
