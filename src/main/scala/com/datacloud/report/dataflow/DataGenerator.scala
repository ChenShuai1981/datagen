package com.datacloud.report.dataflow

import org.scalacheck.Gen

trait DataGenerator[T] {
  def genData: Gen[T]
}
