package com.datacloud.report.dataflow.person

import com.datacloud.report.dataflow.protocol.avro.{Person, PersonDuration}
import org.scalacheck.Gen

object PersonGenerator {

  def genPersonDuration: Gen[PersonDuration] = for {
    years <- Gen.choose(0, 3)
    months <- Gen.choose(0, 11)
    millis <- Gen.choose(1000, 999999)
  } yield {
    import java.nio.ByteBuffer
    import java.nio.ByteOrder
    val bb = ByteBuffer.allocate(32)
    bb.order(ByteOrder.LITTLE_ENDIAN)
    bb.putInt(years)
    bb.putInt(months)
    bb.putInt(millis)
    new PersonDuration(bb.array())
  }

  def genPerson: Gen[Person] = for {
    name <- Gen.identifier
    date <- Gen.choose(1, 30)
    timeMillis <- Gen.choose(1, 100000)
    timestampMillis <- Gen.choose(1234567890L, 9876543210L)
    personDuration <- genPersonDuration
  } yield {
    val person = new Person()
    person.setName(name)
    person.setDate(date)
    person.setTimeMillis(timeMillis)
    person.setTimestampMillis(timestampMillis)
    person.setDuration(personDuration)
    person
  }
}
