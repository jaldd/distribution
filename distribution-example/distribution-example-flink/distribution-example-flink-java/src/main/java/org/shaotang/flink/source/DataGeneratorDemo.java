package org.shaotang.flink.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class DataGeneratorDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(2);
        DataGeneratorSource<String> source = new DataGeneratorSource<>(
                (GeneratorFunction<Long, String>) value -> "longvalue:" + value, 10,
                RateLimiterStrategy.perSecond(1), Types.STRING);
        environment.fromSource(source, WatermarkStrategy.noWatermarks(), "data-generator").print();
        environment.execute();
    }
}
