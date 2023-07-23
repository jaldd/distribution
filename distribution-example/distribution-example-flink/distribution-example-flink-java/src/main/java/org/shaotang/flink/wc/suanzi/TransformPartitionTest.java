package org.shaotang.flink.wc.suanzi;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransformPartitionTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        DataStreamSource<Event> streamSource = environment.fromElements(
                new Event("a", "/a1", 200),
                new Event("a", "/a21", 300),
                new Event("a", "/a3", 200),
                new Event("a", "/b111", 200),
                new Event("b", "/b1", 300),
                new Event("b", "/b111", 100),
                new Event("b", "/b", 100),
                new Event("b", "/b21", 100),
                new Event("b", "/b211111", 100),
                new Event("a", "/a3", 200));

        //随机
//        streamSource.shuffle().print().setParallelism(4);
        //轮训
        streamSource.rebalance().print().setParallelism(4);

        environment.execute();
    }

}
