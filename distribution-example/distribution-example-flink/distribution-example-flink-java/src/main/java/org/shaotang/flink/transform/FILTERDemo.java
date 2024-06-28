package org.shaotang.flink.transform;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.shaotang.flink.bean.WaterSensor;

public class FILTERDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        DataStreamSource<WaterSensor> source = environment.fromElements(
                new WaterSensor("a", 1L, 1),
                new WaterSensor("a", 11L, 11),
                new WaterSensor("b", 2L, 2),
                new WaterSensor("c", 3L, 3)
        );

        SingleOutputStreamOperator<WaterSensor> filter = source.filter(
                new FilterFunction<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor waterSensor) throws Exception {
                        return "a".equals(waterSensor.getId());
                    }
                });
        filter.print();
        environment.execute();
    }
}
