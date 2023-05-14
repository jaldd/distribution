package org.shaotang.flink.wc.suanzi;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class TransformFlatMapTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        DataStreamSource<Event> streamSource = environment.fromElements(
                new Event("a", "/a", 200),
                new Event("b", "/b", 100));
        SingleOutputStreamOperator<String> map = streamSource.flatMap(new MyFlatMapper());

        map.print("1");

        streamSource.flatMap((FlatMapFunction<Event, String>) (event, collector) -> {
            collector.collect(event.getUrl());
            collector.collect(event.getUser());
            collector.collect(String.valueOf(event.getTimestamp()));
        }).returns(new TypeHint<String>(){}).print("2");


        environment.execute();
    }

    public static class MyFlatMapper implements FlatMapFunction<Event, String> {

        @Override
        public void flatMap(Event event, Collector<String> collector) {
            collector.collect(event.getUrl());
            collector.collect(event.getUser());
            collector.collect(String.valueOf(event.getTimestamp()));
        }
    }
}
