package com.datacloud.datagen.feedback

import java.text.SimpleDateFormat

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.datagen.history.genCertNo
import com.datacloud.polaris.protocol.avro.{ApplyRejectedEvent, Region}
import org.scalacheck.Gen

object ApplyRejectedEventProducer extends App {

//  val topicName = "loc_APPLY_REJECTED_EVENT"
//  val bootstrapServers = "localhost:9092"
//  val schemaRegistryUrl = "http://localhost:8081"

  val topicName = "dev_APPLY_REJECTED_EVENT"
  val bootstrapServers = "10.12.0.131:9092"
  val schemaRegistryUrl = "http://10.12.0.131:8081"

//    val topicName = "preprod_APPLY_REJECTED_EVENT"
//    val bootstrapServers = "10.12.0.6:9092"
//    val schemaRegistryUrl = "http://10.12.0.6:8081"

//  val topicName = "preprod_APPLY_REJECTED_EVENT"
//  val bootstrapServers = "10.12.0.175:9092"
//  val schemaRegistryUrl = "http://10.12.0.175:8081"

  val producer = new ApplyRejectedEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 100L, 100)
  producer.run()
}

class ApplyRejectedEventProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[ApplyRejectedEvent](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def genOccurTime: Gen[Long] = for {
    dateString <- Gen.choose(21, 22).map(day => if (day < 10) "0"+day.toString else day.toString)
    hourString <- Gen.choose(14, 20).map(hour => if (hour < 10) "0"+hour.toString else hour.toString)
  } yield {
    val ds = "2018-06-" + dateString + " " + hourString + ":00:00"
    //    println(ds)
    sdf.parse(ds).getTime
  }

  def genData: Gen[ApplyRejectedEvent] = {
    for {
      tenantId <- Gen.oneOf(Seq(2557L))
      productCode <- Gen.const("test")
      terminal <- Gen.const("GENERAL")
      riskProcessId <- Gen.choose(1234560000L, 1234569999L)
      name <- Gen.identifier
      certNo <- genCertNo
      region <- genRegion
      phone <- Gen.choose(13512393721L, 13821382136L).map(_.toString)
      applyTime <- genOccurTime
      applyAmount <- Gen.choose(1000L, 10000L)
      eventTime <- Gen.const(System.currentTimeMillis())
    } yield {
      val applyRejectedEvent = new ApplyRejectedEvent()
      applyRejectedEvent.setTenantId(tenantId)
      applyRejectedEvent.setProductCode(productCode)
      applyRejectedEvent.setTerminal(terminal)
      applyRejectedEvent.setRiskProcessId(riskProcessId)
      applyRejectedEvent.setRiskProcessId(2361283771L)
      applyRejectedEvent.setName(name)
      applyRejectedEvent.setCertNo(certNo)
      applyRejectedEvent.setCertNo("362502198101110613")
      applyRejectedEvent.setPhone(phone)
      applyRejectedEvent.setPhoneCleaned(phone)
      applyRejectedEvent.setRegion(region)
      applyRejectedEvent.setRegion(Region.INDONESIA)
      applyRejectedEvent.setApplyTime(applyTime)
      applyRejectedEvent.setApplyAmount(applyAmount)
      applyRejectedEvent.setEventTime(eventTime)

      applyRejectedEvent
    }
  }

  override def getKey(t: ApplyRejectedEvent): String = s"${t.getRiskProcessId}"
}
