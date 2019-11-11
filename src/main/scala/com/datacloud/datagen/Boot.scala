//package com.datacloud.datagen
//
//import com.datacloud.datagen.feedback.{LoanEndEventProducer, OverdueEventProducer, PaymentEventProducer}
//import com.datacloud.datagen.history.{CreditInvocationHistoryProducer, RiskInvocationHistoryProducer}
//import com.datacloud.datagen.market.MarketDecisionResultProducer
//
//object Boot {
//
//  def main(args: Array[String]) = {
//    if (args.length < 4) {
//      println("run with parameters: SetupKafkaTopic [eventType] [topicName] [bootstrapServers] [schemaRegistryUrl] ")
//      println("valid eventTypes: CreditInvocationHistory, RiskInvocationHistory, OverdueEvent, PaymentEvent, LoanEndEvent, MarketDecisionResult")
//      System.exit(0)
//    }
//
//    val eventType = args(0) // CreditInvocationHistory | OverdueEvent | RiskInvocationHistory
//    val topicName = args(1) // mytopic
//    val bootstrapServers = args(2) // localhost:9092
//    val schemaRegistryUrl = args(3) // http://localhost:8081
//
//    val producer = eventType match {
//      case "CreditInvocationHistory" => new CreditInvocationHistoryProducer(topicName, bootstrapServers, schemaRegistryUrl, 600, 1)
//      case "RiskInvocationHistory" => new RiskInvocationHistoryProducer(topicName, bootstrapServers, schemaRegistryUrl, 600, 1)
//      case "OverdueEvent" => new OverdueEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 600, 1)
//      case "PaymentEvent" => new PaymentEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 600, 1)
//      case "LoanEndEvent" => new LoanEndEventProducer(topicName, bootstrapServers, schemaRegistryUrl, 600, 1)
//      case "MarketDecisionResult" => new MarketDecisionResultProducer(topicName, bootstrapServers, 60, 1)
//      case _ => throw new RuntimeException("unknow event type. Valid event types are: CreditInvocationHistory, RiskInvocationHistory, OverdueEvent, PaymentEvent, LoanEndEvent, MarketDecisionResult")
//    }
//    producer.run()
//  }
//
//}
