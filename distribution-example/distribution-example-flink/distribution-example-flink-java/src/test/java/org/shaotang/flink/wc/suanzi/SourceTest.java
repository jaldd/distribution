package org.shaotang.flink.wc.suanzi;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;
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

        //第一个参数就一个名字，第二个参数用来表示事件时间
        SingleOutputStreamOperator<Tuple2<String, Long>> initData = streamSource.map((MapFunction<String, Tuple2<String, Long>>) value -> {
            String[] s = value.split(",");
            //假设我们在控制台输入的参数是a 15s,那么我们要15*1000才能得到时间戳的毫秒时间
            return Tuple2.of(s[0], Long.parseLong(s[2]) * 1000L);
        });

        //设置水位线
        SingleOutputStreamOperator<Tuple2<String, Long>> watermarks = initData.assignTimestampsAndWatermarks(
                // 针对乱序流插入水位线，延迟时间设置为 2s
                WatermarkStrategy.<Tuple2<String, Long>>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                        .withTimestampAssigner((SerializableTimestampAssigner<Tuple2<String, Long>>) (element, recordTimestamp) -> {
                            //指定事件时间
                            return element.f1;
                        })
        );


        //在普通的datastream的api搞不定的时候就可以使用它了
        //定义a的分流标签
        OutputTag<Tuple2<String, Long>> a = new OutputTag<Tuple2<String, Long>>("a") {
        };
        //定义b的分流标签
        OutputTag<Tuple2<String, Long>> b = new OutputTag<Tuple2<String, Long>>("b") {
        };

        SingleOutputStreamOperator<Tuple2<String, Long>> res = watermarks.process(new ProcessFunction<Tuple2<String, Long>, Tuple2<String, Long>>() {
            @Override
            public void processElement(Tuple2<String, Long> value, Context ctx, Collector<Tuple2<String, Long>> out) throws Exception {
                if (value.f0.equals("a")) {
                    //输出到对应的a标签
                    ctx.output(a, value);
                } else if (value.f0.equals("b")) {
                    //输出到对应的b的标签
                    ctx.output(b, value);
                }
            }
        });

        //得到分流a的结果
        DataStream<Tuple2<String, Long>> aDStream = res.getSideOutput(a);
        aDStream.print("a: ");

        //得到分流b的结果
        DataStream<Tuple2<String, Long>> bDStream = res.getSideOutput(b);
        bDStream.print("b: ");

        environment.execute();


//        environment.execute();
    }
}
