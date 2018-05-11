package com.datacloud.report.dataflow.overdueevent

import java.util.Calendar

import com.datacloud.polaris.protocol.avro.OverdueEvent
import org.scalacheck.Gen

object OverdueEventGenerator {

  def genOverdueEvent(): Gen[OverdueEvent] = for {
    riskProcessId <- Gen.choose(1234560000L, 1234569999L)
    terminal <- Gen.const("GENERAL")
    productCode <- Gen.const("test")
    tenantId <- Gen.const(8L)
    name <- Gen.identifier
    certNo <- Gen.oneOf("382381236723123", "421312398123123", "2138123721837")
    phone <- Gen.oneOf("13213927213", "13301293124", "15384238423")
    overdueAmount <- Gen.choose(1000d, 10000d)
    overdueDays <- Gen.choose(0, 30)
    eventTime <- Gen.const(System.currentTimeMillis())
  } yield {
    val overdueEvent = new OverdueEvent()
    overdueEvent.setCertNo(certNo)
    val dueCal = Calendar.getInstance()
    dueCal.setTimeInMillis(eventTime)
    dueCal.add(Calendar.DATE, -overdueDays)
    overdueEvent.setDueDate(dueCal.getTime.getTime)
    overdueEvent.setEventTime(eventTime)
    overdueEvent.setName(name)
    overdueEvent.setOverdueAmount(overdueAmount)
    overdueEvent.setOverdueDays(overdueDays)
    overdueEvent.setPhone(phone)
    overdueEvent.setProductCode(productCode)
    overdueEvent.setRiskProcessId(riskProcessId)
    overdueEvent.setTenantId(tenantId)
    overdueEvent.setTerminal(terminal)

    overdueEvent
  }

}
