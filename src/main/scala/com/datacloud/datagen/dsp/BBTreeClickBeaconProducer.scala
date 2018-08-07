package com.datacloud.datagen.dsp

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.dsp.avro.bbtree.{BBTreeClickBeaconObj, BBTreeImpBeaconObj}
import org.scalacheck.Gen

object BBTreeClickBeaconObjProducer extends App {
  val topicName = "loc_BBTREE_CLICK_BEACON"
  val bootstrapServers = "localhost:9092"
  val schemaRegistryUrl = "http://localhost:8081"

  val producer = new BBTreeClickBeaconObjProducer(topicName, bootstrapServers, schemaRegistryUrl, 600L, 10)
  producer.run()
}

class BBTreeClickBeaconObjProducer (topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[BBTreeClickBeaconObj](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[BBTreeClickBeaconObj] = genBBTreeClickBeaconObj

  override def getKey(t: BBTreeClickBeaconObj): String = s"${t.getBidid}_${t.getPlacementid}_${t.getTs}"
}
