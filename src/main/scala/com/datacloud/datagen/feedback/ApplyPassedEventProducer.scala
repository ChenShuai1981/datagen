package com.datacloud.datagen.feedback

import java.text.SimpleDateFormat

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.datagen.history.genCertNo
import com.datacloud.polaris.protocol.avro.{ApplyPassedEvent, Region}
import org.scalacheck.Gen

object ApplyPassedEventProducer extends App {

//  val topicName = "loc_APPLY_PASSED_EVENT"
//  val bootstrapServers = "localhost:9092"
//  val schemaRegistryUrl = "http://localhost:8081"

  val topicName = "dev_APPLY_PASSED_EVENT"
  val bootstrapServers = "10.12.0.131:9092"
  val schemaRegistryUrl = "http://10.12.0.131:8081"

//    val topicName = "preprod_APPLY_PASSED_EVENT"
//    val bootstrapServers = "10.12.0.6:9092"
//    val schemaRegistryUrl = "http://10.12.0.6:8081"

//  val topicName = "preprod_APPLY_PASSED_EVENT"
//  val bootstrapServers = "10.12.0.175:9092"
//  val schemaRegistryUrl = "http://10.12.0.175:8081"

  val producer = new ApplyPassedEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 100L, 100)
  producer.run()
}

class ApplyPassedEventProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[ApplyPassedEvent](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def genOccurTime: Gen[Long] = for {
    dateString <- Gen.choose(21, 22).map(day => if (day < 10) "0"+day.toString else day.toString)
    hourString <- Gen.choose(14, 20).map(hour => if (hour < 10) "0"+hour.toString else hour.toString)
  } yield {
    val ds = "2018-06-" + dateString + " " + hourString + ":00:00"
    //    println(ds)
    sdf.parse(ds).getTime
  }

  def genData: Gen[ApplyPassedEvent] = {
    for {
      tenantId <- Gen.choose(2557L, 2560L)
      productCode <- Gen.const("test")
      terminal <- Gen.const("GENERAL")
      riskProcessId <- Gen.choose(811111153L, 911111153L)
      name <- Gen.identifier
      certNo <- genCertNo
      region <- genRegion
      phone <- Gen.choose(13512393721L, 13821382136L).map(_.toString)
      applyTime <- genOccurTime
      applyAmount <- Gen.choose(1000L, 10000L)
      grantAmount = applyAmount - 500d
      eventTime <- Gen.const(System.currentTimeMillis())
    } yield {
      val applyPassedEvent = new ApplyPassedEvent()
      applyPassedEvent.setTenantId(tenantId)
      applyPassedEvent.setProductCode(productCode)
      applyPassedEvent.setTerminal(terminal)
      applyPassedEvent.setRiskProcessId(riskProcessId)
//      applyPassedEvent.setRiskProcessId(57718523L)
      applyPassedEvent.setName(name)
      applyPassedEvent.setCertNo(certNo)
      applyPassedEvent.setCertNo("362502198101110613")
      applyPassedEvent.setPhone(phone)
      applyPassedEvent.setPhoneCleaned(phone)
      applyPassedEvent.setRegion(region)
      applyPassedEvent.setRegion(Region.PRC)
      applyPassedEvent.setApplyTime(applyTime)
      applyPassedEvent.setApplyAmount(applyAmount)
      applyPassedEvent.setEventTime(eventTime)
      applyPassedEvent.setGrantAmount(grantAmount)

      applyPassedEvent
    }
  }

  override def getKey(t: ApplyPassedEvent): String = s"${t.getRiskProcessId}"
}
