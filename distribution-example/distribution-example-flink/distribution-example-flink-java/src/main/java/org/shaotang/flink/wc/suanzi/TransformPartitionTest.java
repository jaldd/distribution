package org.shaotang.flink.wc.suanzi;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.functions.KeySelector;
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
//        streamSource.rebalance().print().setParallelism(4);
        //rescale 小组内重新分配
//        environment.addSource(new RichParallelSourceFunction<Integer>() {
//                    @Override
//                    public void run(SourceContext<Integer> sourceContext) throws Exception {
//                        for (int i = 0; i < 9; i++) {
//                            if (i % 2 == getRuntimeContext().getIndexOfThisSubtask()) {
//                                sourceContext.collect(i);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void cancel() {
//
//                    }
//                }).setParallelism(2)
////                .rescale().print()
//                .setParallelism(4);
        //广播
//        streamSource.broadcast().print().setParallelism(4);
        //全局
//        streamSource.global().print().setParallelism(4);
        //自定义分区
        environment.fromElements(1, 2, 3, 4, 5, 6, 7, 8)
                .partitionCustom(new Partitioner<Integer>() {
                    @Override
                    public int partition(Integer key, int i) {
                        return key % 2;
                    }
                }, new KeySelector<Integer, Integer>() {
                    @Override
                    public Integer getKey(Integer integer) throws Exception {
                        return integer;
                    }
                }).print().setParallelism(4);

        environment.execute();
    }

}
