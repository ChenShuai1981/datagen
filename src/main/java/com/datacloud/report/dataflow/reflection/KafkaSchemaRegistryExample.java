//package com.datacloud.report.dataflow.reflection;
//
//import org.apache.avro.Schema;
//import org.apache.avro.generic.GenericDatumReader;
//import org.apache.avro.generic.GenericRecord;
//import org.apache.avro.io.DecoderFactory;
//import org.apache.avro.io.EncoderFactory;
//import org.apache.avro.reflect.ReflectData;
//import org.apache.avro.reflect.ReflectDatumWriter;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.Properties;
//import java.util.concurrent.ExecutionException;
//
//enum FooEnum {
//    E1, E2, E3;
//}
//
//class FooBase {
//    private String baseValue;
//
//    private FooEnum fooEnum;
//
//    public FooEnum getFooEnum() {
//        return fooEnum;
//    }
//
//    void setFooEnum(FooEnum fooEnum) {
//        this.fooEnum = fooEnum;
//    }
//
//    public String getBaseValue() {
//        return baseValue;
//    }
//
//    void setBaseValue(String baseValue) {
//        this.baseValue = baseValue;
//    }
//}
//
//class FooChild extends FooBase {
//    private String childValue;
//
//    public String getChildValue() {
//        return childValue;
//    }
//
//    void setChildValue(String childValue) {
//        this.childValue = childValue;
//    }
//}
//
//
//class Bar {
//    private FooChild foo;
//
//    public FooChild getFoo() {
//        return foo;
//    }
//
//    public void setFoo(FooChild foo) {
//        this.foo = foo;
//    }
//}
//
//public class KafkaSchemaRegistryExample {
//
//    private static final String schemaRegistryUrl = "http://localhost:8081";
//
//    private static final String kafkaBootstrap = "localhost:9092";
//
//    private static final String topic = "fuweioutput";
//
//    private static final Schema avroSchema = ReflectData.AllowNull.get().getSchema(FuweiOutput.class);
//
//    private static final ReflectDatumWriter<FuweiOutput> reflectDatumWriter = new ReflectDatumWriter<>(avroSchema);
//
//    private static final GenericDatumReader<Object> genericRecordReader = new GenericDatumReader<>(avroSchema);
//
//    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
//
//        // prepare properties for KafkaProducer
//        Properties props = new Properties();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrap);
//        props.put(ProducerConfig.ACKS_CONFIG, "all");
//        props.put(ProducerConfig.RETRIES_CONFIG, 0);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
//        props.put("schema.registry.url", schemaRegistryUrl);
//        KafkaProducer<Object, Object> producer = new KafkaProducer<>(props);
//
//        // prepare example instance
//        FuweiOutput instance = new FuweiOutput();
//        instance.setClientData_principalAmount(new BigDecimal("102138.23"));
//        instance.setClientData_bankCard("bankCard");
//        instance.setClientData_deviceInfo_isSimulator(false);
//        instance.setClientData_deviceInfo_osVersion("2.3.4");
//        instance.setClientData_deviceInfo_country("CN");
//        instance.setClientData_deviceInfo_networkType("4G");
//        instance.setClientData_deviceInfo_macAddress("CN:KD:ER:AS:ZB");
//        instance.set
//        instance.setClientData_callLogs_0_date();
//
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        reflectDatumWriter.write(instance, EncoderFactory.get().directBinaryEncoder(bytes, null));
//        GenericRecord genericRecord = (GenericRecord) genericRecordReader.read(null, DecoderFactory.get().binaryDecoder(bytes.toByteArray(), null));
//
//        ProducerRecord<Object, Object> record = new ProducerRecord<>(topic, "eventKey", genericRecord);
//        producer.send(record).get();
//    }
//
//}