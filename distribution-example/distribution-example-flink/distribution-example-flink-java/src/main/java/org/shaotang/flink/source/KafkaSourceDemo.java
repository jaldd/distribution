package org.shaotang.flink.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class KafkaSourceDemo {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        DeserializationSchema<String> schema = new SimpleStringSchema();
        KafkaSource<String> kafkaSourceBuilder = KafkaSource.<String>builder().setBootstrapServers("kafka001:6667")
                .setGroupId("testflinkgroup1").setTopics("testtopic1")
                .setValueOnlyDeserializer(schema)
                .setStartingOffsets(OffsetsInitializer.latest()).build();
        environment.fromSource(kafkaSourceBuilder, WatermarkStrategy.noWatermarks(), "kfsource").print();
        environment.execute();
    }
}
