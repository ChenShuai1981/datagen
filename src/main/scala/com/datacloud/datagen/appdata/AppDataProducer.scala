package com.datacloud.datagen.appdata

import java.time.LocalDateTime

import com.datacloud.datagen.{JsonDataProducer, KafkaEnv}
import org.scalacheck.Gen

object AppDataProducer extends App with KafkaEnv {
  val topicName = envPrefix + "app_behavior_collector_detail"
  val producer = new AppDataProducer(topicName, bootstrapServers, 600, 1)
  producer.run()
}

case class AppData(id: Long, clientId: String, tenantId: Long, region: String, indivID: String,
                              indivPhone: String, indivName: String, clientData_bankCard: String,
                              clientData_deviceInfo_generalDeviceId: String,
                              indivEmergentContacts_0_phone: List[String],
                              indivEmergentContacts_0_relation: List[String],
                              clientData_contacts_0_phones_0_value: List[List[String]],
                              clientData_deviceInfo_ipAddress: String,
                              clientData_locationInfo_lat: Double,
                              clientData_locationInfo_lng: Double,
                              clientData_message_0_address: List[String],
                              clientData_message_0_body: List[String],
                              clientData_message_0_date: List[LocalDateTime],
                              clientData_message_0_status: List[String],
                              clientData_message_0_type: List[String])

class AppDataProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[AppData](topicName, bootstrapServers, interval, loop) {

  override def genData = for {
    id <- Gen.choose(1000L, 1000000L)
    clientId <- Gen.uuid.map(_.toString)
    tenantId <- Gen.choose(1000L, 1500L)
    region <- Gen.const("VIETNAM") // Gen.oneOf(Seq("INDONESIA", "PRC", "MALAYSIA", "VIETNAM", "UNKNOWN"))
    indivID <- genCertNo
    indivPhone <- Gen.const("142345733335") //genPhone
    indivName <- genName
    clientData_bankCard <- genBankNo
    clientData_deviceInfo_generalDeviceId <- genDeviceId
    indivEmergentContacts_0_phone <- Gen.containerOfN[List, String](2, genPhone)
    indivEmergentContacts_0_relation <- Gen.containerOfN[List, String](2, genRelation)
    clientData_contacts_0_phones_0_value <- Gen.containerOf[List, List[String]](Gen.containerOf[List, String](genPhone))
    clientData_deviceInfo_ipAddress <- genIP
    (clientData_locationInfo_lat, clientData_locationInfo_lng) <- genGPS
    clientData_messages <- genSmsMessages
  } yield {
    val clientDataForStats = AppData(id, clientId, tenantId, region, indivID, indivPhone, indivName, clientData_bankCard.orNull,
      clientData_deviceInfo_generalDeviceId.orNull,
      indivEmergentContacts_0_phone,
      indivEmergentContacts_0_relation,
      clientData_contacts_0_phones_0_value,
      clientData_deviceInfo_ipAddress,
      clientData_locationInfo_lat,
      clientData_locationInfo_lng,
      clientData_messages.map(_._1),
      clientData_messages.map(_._2),
      clientData_messages.map(_._3),
      clientData_messages.map(_._4),
      clientData_messages.map(_._5)
    )

    clientDataForStats
  }

  override def getKey(t: AppData): String = t.id.toString
}