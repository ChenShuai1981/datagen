package com.datacloud.report.dataflow.reflection

import org.scalatest.prop.GeneratorDrivenPropertyChecks

object FuweiOutputCreator extends App with GeneratorDrivenPropertyChecks {

  def getFuweiOutputs: List[FuweiOutput] = {
    val outputs = scala.collection.mutable.ListBuffer[FuweiOutput]()
    forAll(FuweiOutputGenerator.genFuweiOutput) {
      (output: FuweiOutput) => {
        outputs += output
      }
    }
    outputs.toList
  }

  getFuweiOutputs.foreach(output => println(output))
}
