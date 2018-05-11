package com.datacloud.report.dataflow.riskinvocationhistory

import com.datacloud.polaris.protocol.avro.RiskInvocationHistory
import com.datacloud.report.dataflow.util.JsonUtil
import org.scalatest.prop.GeneratorDrivenPropertyChecks

object RiskInvocationHistoryCreator extends App with GeneratorDrivenPropertyChecks {

  def getRiskInvocationHistories: List[RiskInvocationHistory] = {
    val riskInvocationHistories = scala.collection.mutable.ListBuffer[RiskInvocationHistory]()
    forAll(RiskInvocationHistoryGenerator.genRiskInvocationHistory) {
      (riskInvocationHistory: RiskInvocationHistory) => {
        riskInvocationHistories += riskInvocationHistory
      }
    }
    riskInvocationHistories.toList
  }


  getRiskInvocationHistories.foreach(history => println(JsonUtil.toJson(history)))
}
