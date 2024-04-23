package org.shaotang.flink.source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CollectionDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> streamSource = environment.
        fromElements("a","b","c")
//                fromCollection(Arrays.asList("a", "b", "c"))
                ;
        streamSource.print();
        environment.execute();
    }
}
