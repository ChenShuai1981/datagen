package com.datacloud.datagen.clientdata

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
                              clientData_contacts_0_phones_0_value: List[List[String]])

class ClientDataForStatsProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[ClientDataForStats](topicName, bootstrapServers, interval, loop) {

  override def genData = for {
    id <- Gen.choose(1000L, 1000000L)
    clientId <- Gen.uuid.map(_.toString)
    tenantId <- Gen.choose(1000L, 1500L)
    region <- Gen.oneOf(Seq("INDONESIA", "PRC", "MALAYSIA", "VIETNAM", "UNKNOWN"))
    indivID <- genCertNo
    indivPhone <- genPhone
    indivName <- genName
    clientData_bankCard <- genBankNo
    clientData_deviceInfo_generalDeviceId <- genDeviceId
    indivEmergentContacts_0_phone <- Gen.containerOfN[List, String](2, genPhone)
    clientData_contacts_0_phones_0_value <- Gen.containerOf[List, List[String]](Gen.containerOf[List, String](genPhone))
  } yield {
    val clientDataForStats = ClientDataForStats(id, clientId, tenantId, region, indivID, indivPhone, indivName, clientData_bankCard.orNull,
      clientData_deviceInfo_generalDeviceId.orNull,
      indivEmergentContacts_0_phone,
      clientData_contacts_0_phones_0_value)

    clientDataForStats
      .copy(indivPhone = "13921938217")
      .copy(region = "VIETNAM")
      .copy(indivID = "191848198801274444")
      .copy(clientId = "96dac856-7e6c-4ca5-b14d-5b6b98be303c")
      .copy(clientData_deviceInfo_generalDeviceId = "adid_298429382174")
      .copy(indivEmergentContacts_0_phone = List("13921938218", "13266868337"))
  }

  override def getKey(t: ClientDataForStats): String = t.id.toString
}
