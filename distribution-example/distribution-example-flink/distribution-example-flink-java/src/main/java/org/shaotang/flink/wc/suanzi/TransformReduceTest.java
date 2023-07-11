package org.shaotang.flink.wc.suanzi;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

public class TransformReduceTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        DataStreamSource<Event> streamSource = environment.fromElements(
                new Event("a", "/a1", 200),
                new Event("a", "/a2", 300),
                new Event("a", "/a3", 200),
                new Event("a", "/b1", 200),
                new Event("b", "/b1", 300),
                new Event("b", "/b1", 100),
                new Event("b", "/b2", 100),
                new Event("b", "/b2", 100),
                new Event("b", "/b2", 100),
                new Event("a", "/a3", 200));

        SingleOutputStreamOperator<Tuple2<String, Long>> reduce = streamSource
                //用lambda会报错
                .map(new MapFunction<Event, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(Event event) {
                        return Tuple2.of(event.getUser(), 1L);
                    }
                }).keyBy(data -> data.f0).reduce((ReduceFunction<Tuple2<String, Long>>) (tuple1, tuple2) ->
                        Tuple2.of(tuple1.f0, tuple1.f1 + tuple1.f1));


        SingleOutputStreamOperator<Tuple2<String, Long>> result = reduce.keyBy(data -> "key")
                .reduce((ReduceFunction<Tuple2<String, Long>>) (tuple2, t1) -> tuple2.f1 > t1.f1 ? tuple2 : t1);
        result.print();
        environment.execute();
    }

}
