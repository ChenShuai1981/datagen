package com.datacloud.report.dataflow.market

import com.datacloud.report.dataflow.util.JsonUtil
import org.scalatest.prop.GeneratorDrivenPropertyChecks

object GainActivityRightEventCreator extends App with GeneratorDrivenPropertyChecks {

  def getGainActivityRightEvents: List[GainActivityRightEvent] = {
    val gainActivityRightEvents = scala.collection.mutable.ListBuffer[GainActivityRightEvent]()
    forAll(GainActivityRightEventGenerator.genGainActivityRightEvent) {
      (gainActivityRightEvent: GainActivityRightEvent) => {
        gainActivityRightEvents += gainActivityRightEvent
      }
    }
    gainActivityRightEvents.toList
  }


  getGainActivityRightEvents.foreach(result => println(JsonUtil.toJson(result)))
}
