package com.datacloud.datagen.util

import java.time.{LocalDate, LocalDateTime}

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.{LocalDateSerializer, LocalDateTimeSerializer}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

object JsonUtil {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
  val javaTimeModule = new JavaTimeModule
  javaTimeModule.addSerializer(classOf[LocalDateTime], new LocalDateTimeSerializer(null))
  javaTimeModule.addSerializer(classOf[LocalDate], new LocalDateSerializer(null))
  mapper.registerModule(javaTimeModule)

  def toJson(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k,v) => k.name -> v})
  }

  def toJson(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def toMap[V](json:String)(implicit m: Manifest[V]) = fromJson[Map[String,V]](json)

  def fromJson[T](json: String)(implicit m : Manifest[T]): T = {
    mapper.readValue[T](json)
  }
}