package org.shaotang.flink.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.shaotang.flink.bean.WaterSensor;

public class MapDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        DataStreamSource<WaterSensor> source = environment.fromElements(
                new WaterSensor("a", 1L, 1),
                new WaterSensor("b", 2L, 2),
                new WaterSensor("c", 3L, 3)
        );

        SingleOutputStreamOperator<String> map = source.map((MapFunction<WaterSensor, String>) WaterSensor::getId);
        map.print();
        environment.execute();
    }
}
