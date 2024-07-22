package org.shaotang.flink.aggreate;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.shaotang.flink.bean.WaterSensor;

public class KeyBySumDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        DataStreamSource<WaterSensor> source = environment.fromElements(
                new WaterSensor("a", 1L, 1),
                new WaterSensor("a", 11L, 11),
                new WaterSensor("b", 2L, 2),
                new WaterSensor("c", 3L, 3)
        );

        KeyedStream<WaterSensor, String> waterSensorStringKeyedStream = source.keyBy(new KeySelector<WaterSensor, String>() {
            @Override
            public String getKey(WaterSensor waterSensor) throws Exception {
                return waterSensor.getId();
            }
        });
        SingleOutputStreamOperator<WaterSensor> sum = waterSensorStringKeyedStream.sum("vc");
        sum.print();

        environment.execute();
    }
}
