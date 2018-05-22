package com.datacloud.datagen.util

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, ZoneId}

object DateTimeUtil {

  val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  def formatDateTime(epochMilli: Long): String = {
    Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).format(dateTimeFormatter)
  }

  def parseDateTime(datetime: String): Long = {
    LocalDateTime.parse(datetime, dateTimeFormatter).atZone(ZoneId.systemDefault()).toInstant.toEpochMilli
  }

  def formatDate(epochMilli: Long): String = {
    Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).format(dateFormatter)
  }

  def parseDate(date: String): Long = {
    LocalDateTime.parse(date, dateFormatter).atZone(ZoneId.systemDefault()).toInstant.toEpochMilli
  }
}
