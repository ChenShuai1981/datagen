package com.datacloud.report.dataflow.reflection

import java.time.LocalDateTime
import java.util

import org.scalacheck.Gen

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

object FuweiOutputGenerator {

  def genFuweiOutput: Gen[FuweiOutput] = for {
    clientData_principalAmount <- Gen.choose(100.00, 999999.99).map(java.math.BigDecimal.valueOf(_))
    clientData_bankCard <- Gen.identifier
    clientData_deviceInfo_isSimulator <- Gen.const(false)
    clientData_deviceInfo_osVersion <- Gen.identifier
    clientData_deviceInfo_country <- Gen.identifier
    clientData_deviceInfo_networkType <- Gen.identifier
    clientData_deviceInfo_macAddress <- Gen.identifier
    clientData_deviceInfo_idForVendor <- Gen.identifier
    clientData_deviceInfo_idForAdvertising <- Gen.identifier
    clientData_deviceInfo_carrierName <- Gen.identifier
    clientData_deviceInfo_model <- Gen.identifier
    clientData_deviceInfo_totalMemory <- Gen.identifier
    clientData_deviceInfo_displayResolution <- Gen.identifier
    clientData_deviceInfo_deviceArch <- Gen.identifier
    clientData_deviceInfo_totalStorage <- Gen.identifier
    clientData_deviceInfo_cpuCount <- Gen.choose(1L, 16L)
    clientData_deviceInfo_cpuSpeed <- Gen.identifier
    clientData_deviceInfo_ipAddress <- Gen.identifier
    clientData_locationInfo_cityName <- Gen.identifier
    clientData_locationInfo_lng <- Gen.choose(-180.00, 180.00).map(java.math.BigDecimal.valueOf(_))
    clientData_locationInfo_lat <- Gen.choose(-90.00, 90.00).map(java.math.BigDecimal.valueOf(_))
    clientData_locationInfo_address <- Gen.identifier
    clientData_callLogs_0_name <- Gen.listOf(Gen.identifier)
    clientData_callLogs_0_number <- Gen.listOf(Gen.identifier)
//    clientData_callLogs_0_date = List(LocalDateTime.now())
    clientData_callLogs_0_duration <- Gen.listOf(Gen.identifier)
    clientData_callLogs_0_type <- Gen.listOf(Gen.identifier)
    clientData_installedApps_0_appLabel <- Gen.listOf(Gen.identifier)
//    clientData_installedApps_0_firstInstallTime = List(LocalDateTime.now())
//    clientData_installedApps_0_lastUpdateTime = List(LocalDateTime.now())
    clientData_installedApps_0_packageName <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_displayName <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_firstName <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_middleName <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_lastName <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_jobTitle <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_companyName <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_postalAddresses_label <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_postalAddresses_value <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_phone_label <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_phone_value <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_emails_label <- Gen.listOf(Gen.identifier)
    clientData_contacts_0_emails_value <- Gen.listOf(Gen.identifier)
  } yield {
    val output = new FuweiOutput()
    output.setClientData_bankCard(clientData_bankCard)
    output.setClientData_principalAmount(clientData_principalAmount)
    output.setClientData_deviceInfo_isSimulator(clientData_deviceInfo_isSimulator)
    output.setClientData_deviceInfo_osVersion(clientData_deviceInfo_osVersion)
    output.setClientData_deviceInfo_country(clientData_deviceInfo_country)
    output.setClientData_deviceInfo_networkType(clientData_deviceInfo_networkType)
    output.setClientData_deviceInfo_macAddress(clientData_deviceInfo_macAddress)
    output.setClientData_deviceInfo_idForVendor(clientData_deviceInfo_idForVendor)
    output.setClientData_deviceInfo_idForAdvertising(clientData_deviceInfo_idForAdvertising)
    output.setClientData_deviceInfo_carrierName(clientData_deviceInfo_carrierName)
    output.setClientData_deviceInfo_model(clientData_deviceInfo_model)
    output.setClientData_deviceInfo_totalMemory(clientData_deviceInfo_totalMemory)
    output.setClientData_deviceInfo_displayResolution(clientData_deviceInfo_displayResolution)
    output.setClientData_deviceInfo_deviceArch(clientData_deviceInfo_deviceArch)
    output.setClientData_deviceInfo_totalStorage(clientData_deviceInfo_totalStorage)
    output.setClientData_deviceInfo_cpuCount(clientData_deviceInfo_cpuCount)
    output.setClientData_deviceInfo_cpuSpeed(clientData_deviceInfo_cpuSpeed)
    output.setClientData_deviceInfo_ipAddress(clientData_deviceInfo_ipAddress)
    output.setClientData_locationInfo_cityName(clientData_locationInfo_cityName)
    output.setClientData_locationInfo_lng(clientData_locationInfo_lng)
    output.setClientData_locationInfo_lat(clientData_locationInfo_lat)
    output.setClientData_locationInfo_address(clientData_locationInfo_address)
    output.setClientData_callLogs_0_name(clientData_callLogs_0_name)
    output.setClientData_callLogs_0_number(clientData_callLogs_0_number)
    output.setClientData_callLogs_0_date(util.Arrays.asList(LocalDateTime.now()))
    output.setClientData_callLogs_0_duration(clientData_callLogs_0_duration)
    output.setClientData_callLogs_0_type(clientData_callLogs_0_type)
    output.setClientData_installedApps_0_appLabel(clientData_installedApps_0_appLabel)
    output.setClientData_installedApps_0_firstInstallTime(util.Arrays.asList(LocalDateTime.now()))
    output.setClientData_installedApps_0_lastUpdateTime(util.Arrays.asList(LocalDateTime.now()))
    output.setClientData_installedApps_0_packageName(clientData_installedApps_0_packageName)
    output.setClientData_contacts_0_displayName(clientData_contacts_0_displayName)
    output.setClientData_contacts_0_firstName(clientData_contacts_0_firstName)
    output.setClientData_contacts_0_middleName(clientData_contacts_0_middleName)
    output.setClientData_contacts_0_lastName(clientData_contacts_0_lastName)
    output.setClientData_contacts_0_jobTitle(clientData_contacts_0_jobTitle)
    output.setClientData_contacts_0_companyName(clientData_contacts_0_companyName)
    output.setClientData_contacts_0_postalAddresses_label(clientData_contacts_0_postalAddresses_label)
    output.setClientData_contacts_0_postalAddresses_value(clientData_contacts_0_postalAddresses_value)
    output.setClientData_contacts_0_phone_label(clientData_contacts_0_phone_label)
    output.setClientData_contacts_0_phone_value(clientData_contacts_0_phone_value)
    output.setClientData_contacts_0_emails_label(clientData_contacts_0_emails_label)
    output.setClientData_contacts_0_emails_value(clientData_contacts_0_emails_value)

    output
  }
}
