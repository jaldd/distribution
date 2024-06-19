package org.shaotang.flink.transform;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.shaotang.flink.bean.WaterSensor;

public class FlatMapDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        DataStreamSource<WaterSensor> source = environment.fromElements(
                new WaterSensor("a", 1L, 1),
                new WaterSensor("a", 11L, 11),
                new WaterSensor("b", 2L, 2),
                new WaterSensor("c", 3L, 3)
        );

        SingleOutputStreamOperator<Object> flatMap = source.flatMap(
                new FlatMapFunction<WaterSensor, Object>() {
                    @Override
                    public void flatMap(WaterSensor o, Collector collector) throws Exception {
                        if (StringUtils.equals(o.getId(), "a")) {
                            collector.collect(o.getTs());
                        }
                        if (StringUtils.equals(o.getId(), "b")) {
                            collector.collect(o.getVc());
                        }
                    }
                });
        flatMap.print();
        environment.execute();
    }
}
