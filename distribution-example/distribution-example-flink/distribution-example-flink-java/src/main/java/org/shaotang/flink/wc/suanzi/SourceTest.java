package org.shaotang.flink.wc.suanzi;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SourceTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        DataStreamSource<String> streamSource = environment.readTextFile("distribution-example/distribution-example-flink/distribution-example-flink-java/src/main/resources/input/inputsz/clicks.txt");
        streamSource.print("1");

        environment.execute();
    }
}
