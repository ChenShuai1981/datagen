-- lets the windows accumulate more data
set 'commit.interval.ms'='2000';
set 'cache.max.bytes.buffering'='10000000';
set 'auto.offset.reset'='earliest';

-- 1. SOURCE of RiskDecisionStream
DROP STREAM risk_decision_stream;
CREATE STREAM risk_decision_stream (riskProcessId BIGINT,tenantId INTEGER,userId STRING,productCode STRING,terminal STRING,eventCode STRING,occurTime BIGINT,executionId BIGINT,decision STRING,fraudScore DOUBLE,creditScore DOUBLE,appCode STRING,creditStrategyRCId BIGINT) with (kafka_topic = 'riskdecision2', value_format = 'json', TIMESTAMP = 'occurTime');

DROP TABLE decisions_per_strategy_min_table;
CREATE TABLE decisions_per_strategy_min_table AS SELECT CREDITSTRATEGYRCID, count(*) AS count FROM RISK_DECISION_STREAM window  TUMBLING (size 60 second) GROUP BY CREDITSTRATEGYRCID;

DROP TABLE decisions_per_strategy_min_ts_table;
CREATE TABLE decisions_per_strategy_min_ts_table AS SELECT rowTime AS decision_ts, * FROM decisions_per_strategy_min_table;

-- SELECT DECISION_TS, CREDITSTRATEGYRCID, COUNT FROM decisions_per_strategy_min_ts_table limit 5;

DROP TABLE decisions_per_strategy_decision_min_table;
CREATE TABLE decisions_per_strategy_decision_min_table AS SELECT CREDITSTRATEGYRCID, DECISION, count(*) AS count FROM RISK_DECISION_STREAM window  TUMBLING (size 60 second) GROUP BY CREDITSTRATEGYRCID, DECISION;

DROP TABLE decisions_per_strategy_decision_min_ts_table;
CREATE TABLE decisions_per_strategy_decision_min_ts_table AS SELECT rowTime AS decision_ts, * FROM decisions_per_strategy_decision_min_table;

-- SELECT DECISION_TS, CREDITSTRATEGYRCID, DECISION, COUNT FROM decisions_per_strategy_decision_min_ts_table limit 5;
