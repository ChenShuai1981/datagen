package com.datacloud.report.dataflow.market

import com.datacloud.report.dataflow.util.JsonUtil
import org.scalatest.prop.GeneratorDrivenPropertyChecks

object MarketDecisionResultCreator extends App with GeneratorDrivenPropertyChecks {

  def getMarketDecisionResults: List[MarketDecisionResult] = {
    val marketDecisionResults = scala.collection.mutable.ListBuffer[MarketDecisionResult]()
    forAll(MarketDecisionResultGenerator.genMarketDecisionResult) {
      (marketDecisionResult: MarketDecisionResult) => {
        marketDecisionResults += marketDecisionResult
      }
    }
    marketDecisionResults.toList
  }


  getMarketDecisionResults.foreach(result => println(JsonUtil.toJson(result)))
}