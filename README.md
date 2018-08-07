随机生成kafka消息，方便检查整个数据流连通性并驱动druid数据源创建。

mvn clean package

java -jar target/datagen-jar-with-dependencies.jar OverdueEvent dev_OVERDUE_EVENT 10.12.0.157:9092 http://10.12.0.157:8081
