package com.datacloud.report.dataflow

import com.datacloud.report.dataflow.util.JsonUtil
import org.scalatest.prop.GeneratorDrivenPropertyChecks

trait DataCreator[T] extends App with GeneratorDrivenPropertyChecks with DataGenerator[T] {
  def getDatas: List[T] = {
    val datas = scala.collection.mutable.ListBuffer[T]()
    forAll(genData) {
      (data: T) => {
        datas += data
      }
    }
    datas.toList
  }

  getDatas.foreach(data => println(JsonUtil.toJson(data)))
}
