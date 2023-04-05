package org.shaotang.flink.wc.suanzi;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class ClickSource implements SourceFunction<Event> {

    private Boolean running = true;

    @Override
    public void run(SourceContext<Event> sourceContext) throws Exception {

        while (running) {
            sourceContext.collect(new Event());
        }
    }

    @Override
    public void cancel() {

    }
}
