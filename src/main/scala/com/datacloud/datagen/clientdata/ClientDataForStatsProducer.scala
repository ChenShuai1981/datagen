package com.datacloud.datagen.clientdata

import java.time.LocalDateTime

import com.datacloud.datagen.{JsonDataProducer, KafkaEnv}
import org.scalacheck.Gen

object ClientDataForStatsProducer extends App with KafkaEnv {
  val topicName = envPrefix + "client_data_json"
  val producer = new ClientDataForStatsProducer(topicName, bootstrapServers, 600, 1)
  producer.run()
}

case class ClientDataForStats(id: Long, clientId: String, tenantId: Long, region: String, indivID: String,
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

class ClientDataForStatsProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[ClientDataForStats](topicName, bootstrapServers, interval, loop) {

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
    val clientDataForStats = ClientDataForStats(id, clientId, tenantId, region, indivID, indivPhone, indivName, clientData_bankCard.orNull,
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

//    clientDataForStats
//      .copy(indivPhone = "142345733333") // 13266868311
//      .copy(region = "VIETNAM")
//      .copy(indivID = "362502198001110601")
//      .copy(clientId = "96dac856-7e6c-4ca5-b14d-5b6b98be3022")
//      .copy(clientData_deviceInfo_generalDeviceId = "adid_298429382177")
//      .copy(clientData_bankCard = "bankNo_12345")
//      .copy(indivEmergentContacts_0_phone = List("13988888882"))
//      .copy(indivEmergentContacts_0_relation = List("PARENT"))
//      .copy(clientData_deviceInfo_ipAddress = "10.12.0.33")
//      .copy(clientData_locationInfo_lat = 31.29381238d)
//      .copy(clientData_locationInfo_lng = 120.12838763d)

    clientDataForStats
  }

  override def getKey(t: ClientDataForStats): String = t.id.toString
}
