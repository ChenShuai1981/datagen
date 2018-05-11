package com.datacloud.report.dataflow.creditinvocationhistory

import com.datacloud.polaris.protocol.avro.CreditInvocationHistory
import com.datacloud.report.dataflow.util.JsonUtil
import org.scalatest.prop.GeneratorDrivenPropertyChecks

object CreditInvocationHistoryCreator extends App with GeneratorDrivenPropertyChecks {

  def getCreditInvocationHistories: List[CreditInvocationHistory] = {
    val creditInvocationHistories = scala.collection.mutable.ListBuffer[CreditInvocationHistory]()
    forAll(CreditInvocationHistoryGenerator.genCreditInvocationHistory) {
      (creditInvocationHistory: CreditInvocationHistory) => {
        creditInvocationHistories += creditInvocationHistory
      }
    }
    creditInvocationHistories.toList
  }


  getCreditInvocationHistories.foreach(history => println(JsonUtil.toJson(history)))
}
