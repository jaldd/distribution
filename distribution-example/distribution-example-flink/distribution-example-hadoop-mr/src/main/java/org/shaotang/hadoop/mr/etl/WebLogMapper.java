package org.shaotang.hadoop.mr.etl;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WebLogMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
        String data = value.toString();
        boolean result = parseLong(data, context);
        if (!result) {
            return;
        }
        context.write(value, NullWritable.get());
    }

    private boolean parseLong(String data, Mapper<LongWritable, Text, Text, NullWritable>.Context context) {

        String[] fields = data.split(" ");
        return fields.length > 11;
    }
}
