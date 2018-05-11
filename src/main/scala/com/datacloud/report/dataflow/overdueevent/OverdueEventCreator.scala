package com.datacloud.report.dataflow.overdueevent

import com.datacloud.polaris.protocol.avro.OverdueEvent
import com.datacloud.report.dataflow.util.JsonUtil
import org.scalatest.prop.GeneratorDrivenPropertyChecks

object OverdueEventCreator extends App with GeneratorDrivenPropertyChecks {

  def getOverdueEvents: List[OverdueEvent] = {
    val overdueEvents = scala.collection.mutable.ListBuffer[OverdueEvent]()
    forAll(OverdueEventGenerator.genOverdueEvent) {
      (overdueEvent: OverdueEvent) => {
        overdueEvents += overdueEvent
      }
    }
    overdueEvents.toList
  }


  getOverdueEvents.foreach(overdueEvent => println(JsonUtil.toJson(overdueEvent)))
}
