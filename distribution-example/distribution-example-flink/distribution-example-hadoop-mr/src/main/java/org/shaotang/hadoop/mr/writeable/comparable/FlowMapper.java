package org.shaotang.hadoop.mr.writeable.comparable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMapper extends Mapper<LongWritable, Text, FlowBean, Text> {

    private FlowBean outK = new FlowBean();
    private Text outV = new Text();

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, FlowBean, Text>.Context context) throws IOException, InterruptedException {

        String line = value.toString();

        String[] split = line.split("\t");

        outV.set(split[0]);
        String up = split[1];
        String down = split[2];
        outK.setUpFlow(Long.parseLong(up));
        outK.setDownFlow(Long .parseLong(down));
        outK.setSumFlow();
        context.write(outK,outV );
    }
}
