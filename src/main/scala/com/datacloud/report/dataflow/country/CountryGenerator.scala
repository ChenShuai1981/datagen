package com.datacloud.report.dataflow.country

import com.datacloud.report.dataflow.protocol.avro.{City, Country, Province}
import org.scalacheck.Gen
import scala.collection.JavaConversions._

object CountryGenerator {

  private def genCity: Gen[City] = for {
    name <- Gen.identifier
    population <- Gen.choose(10000, 1000000)
  } yield {
    City.newBuilder().setName(name).setPopulation(population).build()
  }

  private def genProvince: Gen[Province] = for {
    name <- Gen.identifier
    area <- Gen.choose(99.00, 999999.00)
    numOfCities <- Gen.choose(10, 100)
    cities <- Gen.listOfN(numOfCities, genCity)
  } yield {
    Province.newBuilder().setName(name).setArea(area).setCities(cities).build()
  }

  def genCountry: Gen[Country] = for {
    name <- Gen.identifier
    tenant <- Gen.choose(1, 10)
    numOfProvinces <- Gen.choose(5, 20)
    provinces <- Gen.listOfN(numOfProvinces, genProvince)
  } yield {
    Country.newBuilder().setName(name).setProvinces(provinces).setTenant(tenant).setTs(System.currentTimeMillis()).build()
  }

}
