package org.shaotang.flink.wc.suanzi;

import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

import java.util.Random;

public class ParallelCustomSource implements ParallelSourceFunction<Integer> {

    private Boolean running = true;
    private Random random = new Random();

    @Override
    public void run(SourceContext<Integer> sourceContext) throws Exception {
        while (running) {
            sourceContext.collect(random.nextInt());
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
