package org.shaotang.flink.wc.suanzi;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class TransformSimpleAggTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        DataStreamSource<Event> streamSource = environment.fromElements(
                new Event("a", "/a1", 200),
                new Event("a", "/a2", 300),
                new Event("a", "/a3", 200),
                new Event("a", "/a4", 100),
                new Event("a", "/a3", 200),
                new Event("a", "/b1", 200),
                new Event("b", "/b1", 300),
                new Event("b", "/b1", 100),
                new Event("b", "/b2", 100));
        streamSource.keyBy(Event::getUser)
                .max("timestamp").print("max:");
        streamSource.keyBy(Event::getUser)
                .maxBy("timestamp").print("maxBy:");


        environment.execute();
    }

}
