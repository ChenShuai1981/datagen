package com.datacloud.report.dataflow.reflection

import java.io.ByteArrayOutputStream
import java.util.Properties

import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.avro.io.{DecoderFactory, EncoderFactory}
import org.apache.avro.reflect.{ReflectData, ReflectDatumWriter}
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object FuweiOutputProducer extends App {

  val topic = "fuweioutput"

  val props = new Properties

  import io.confluent.kafka.serializers.KafkaAvroSerializer
  import org.apache.kafka.clients.producer.ProducerConfig

  val avroSchema = ReflectData.AllowNull.get.getSchema(classOf[FuweiOutput])
  println(avroSchema.toString(true))
  val reflectDatumWriter = new ReflectDatumWriter[AnyRef](avroSchema)
  val genericRecordReader = new GenericDatumReader[AnyRef](avroSchema)

  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
  props.put(ProducerConfig.ACKS_CONFIG, "all")
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.12.0.193:9092")
  props.put("schema.registry.url", "http://10.12.0.193:8081")

  val producer: Producer[String, AnyRef] = new KafkaProducer[String, AnyRef](props)
  val interval = 200
  val outputs = FuweiOutputCreator.getFuweiOutputs
  outputs.foreach(output => {
    val bytes = new ByteArrayOutputStream
    reflectDatumWriter.write(output, EncoderFactory.get.directBinaryEncoder(bytes, null))
    val genericRecord = genericRecordReader.read(null, DecoderFactory.get.binaryDecoder(bytes.toByteArray, null)).asInstanceOf[GenericRecord]
    val record = new ProducerRecord[String, AnyRef](topic, "eventKey", genericRecord)
    producer.send(record)
    println(record)
    Thread.sleep(interval)
  })

  producer.flush()
  producer.close()
}
