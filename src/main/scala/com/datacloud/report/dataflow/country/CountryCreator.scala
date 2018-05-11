package com.datacloud.report.dataflow.country

import com.datacloud.report.dataflow.protocol.avro.Country
import org.scalatest.prop.GeneratorDrivenPropertyChecks

object CountryCreator extends App with GeneratorDrivenPropertyChecks {

  def getCountries: List[Country] = {
    val countries = scala.collection.mutable.ListBuffer[Country]()
    forAll(CountryGenerator.genCountry) {
      (country: Country) => {
        countries += country
      }
    }
    countries.toList
  }


  getCountries.foreach(country => println(country))
}
