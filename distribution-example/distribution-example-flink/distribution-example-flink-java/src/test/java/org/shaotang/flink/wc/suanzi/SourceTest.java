package org.shaotang.flink.wc.suanzi;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
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

        DataStreamSource<Event> fromElements = environment.fromElements(new Event("b", "/b", 100));
        fromElements.print("3");

        environment.fromElements("a,b,c").print("4");
        environment.fromElements("a,b,c").flatMap((FlatMapFunction<String, String>) (s, collector) -> Arrays.stream(s.split(","))
                .forEach(collector::collect)).returns(Types.STRING).print("5");


        environment.execute();


//        environment.execute();
    }
}
