package com.datacloud.report.dataflow.person

import com.datacloud.report.dataflow.protocol.avro.Person
import com.datacloud.report.dataflow.util.JsonUtil
import org.scalatest.prop.GeneratorDrivenPropertyChecks

object PersonCreator extends App with GeneratorDrivenPropertyChecks {

  def getPersons: List[Person] = {
    val persons = scala.collection.mutable.ListBuffer[Person]()
    forAll(PersonGenerator.genPerson) {
      (person: Person) => {
        persons += person
      }
    }
    persons.toList
  }


  getPersons.foreach(person => println(JsonUtil.toJson(person)))
}
