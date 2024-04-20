package org.shaotang.flink.wc.suanzi;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SourceTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        DataStreamSource<String> streamSource = environment.readTextFile("distribution-example/distribution-example-flink/distribution-example-flink-java/src/main/resources/input/inputsz/clicks.txt");


        List<Event> events = new ArrayList<>();
        events.add(new Event("a", "/a", 100));
        DataStreamSource<Event> fromCollection = environment.fromCollection(events);


        DataStreamSource<Event> fromElements = environment.fromElements(new Event("b", "/b", 100));

        //nc -lk 6666
        DataStreamSource<String> streamSource4 = environment.socketTextStream("127.0.0.1", 6666);


//        streamSource.print("1");
//        fromCollection.print("2");
//        fromElements.print("3");
//        streamSource4.print("4");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "hadoop-pseudo:9092");
        properties.setProperty("group.id", "consumer-group");
        properties.setProperty("auto.offset.reset", "latest");
//        DataStreamSource<String> kafkaStream = environment.addSource(new FlinkKafkaConsumer<String>("test", new SimpleStringSchema(), properties));

        //kafka生产者控制台调试：./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

//        kafkaStream.print();


        environment.execute();
    }
}
