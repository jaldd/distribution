package org.shaotang.flink.wc;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class BatchWordCount {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> lineDataSource = environment.readTextFile("distribution-example/distribution-example-flink/distribution-example-flink-java/src/main/resources/input/inputword/hello.txt");

        //分词转换为二元祖
        FlatMapOperator<String, Tuple2<String, Long>> tuple = lineDataSource.flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
            String[] words = line.split(" ");
            for (String word : words) {
                out.collect(Tuple2.of(word, 1L));
            }
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));

        //分组
        UnsortedGrouping<Tuple2<String, Long>> group = tuple.groupBy(0);

        AggregateOperator<Tuple2<String, Long>> sum =  group.sum(1);

        sum.print();
    }
}
