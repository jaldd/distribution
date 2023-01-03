package org.shaotang.flink.wc.suanzi;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class SourceTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        DataStreamSource<String> streamSource = environment.readTextFile("distribution-example/distribution-example-flink/distribution-example-flink-java/src/main/resources/input/inputsz/clicks.txt");
        streamSource.print("1");


        List<Event> events = new ArrayList<>();
        events.add(new Event("a", "/a", 100));
        DataStreamSource<Event> fromCollection = environment.fromCollection(events);
        fromCollection.print("2");
        environment.execute();
    }
}
