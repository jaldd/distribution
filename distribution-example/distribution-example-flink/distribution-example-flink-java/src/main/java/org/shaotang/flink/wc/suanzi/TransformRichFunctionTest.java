package org.shaotang.flink.wc.suanzi;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransformRichFunctionTest {
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

        streamSource.map(new RichMapper()).setParallelism(2).print();
        environment.execute();
    }

    public static class RichMapper extends RichMapFunction<Event, Integer> {
        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            System.out.println("open---:"+getRuntimeContext().getIndexOfThisSubtask());
        }

        @Override
        public Integer map(Event event) throws Exception {
            return event.getUrl().length();
        }

        @Override
        public void close() throws Exception {
            super.close();
            System.out.println("close---:"+getRuntimeContext().getIndexOfThisSubtask());
        }
    }

}
