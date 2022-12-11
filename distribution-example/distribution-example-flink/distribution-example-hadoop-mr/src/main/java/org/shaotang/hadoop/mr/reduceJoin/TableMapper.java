package org.shaotang.hadoop.mr.reduceJoin;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class TableMapper extends Mapper<LongWritable, Text, Text, TableBean> {

    private String name;
    private Text outKey = new Text();
    private TableBean outValue = new TableBean();

    @Override
    protected void setup(Mapper<LongWritable, Text, Text, TableBean>.Context context) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        name = fileSplit.getPath().getName();
    }

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, TableBean>.Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] split = line.split("\t");
        if (name.contains("order")) {
            outKey.set(split[1]);
            outValue.setId(split[0]);
            outValue.setPid(split[1]);
            outValue.setAmount(Integer.parseInt(split[2]));
            outValue.setPname("");
            outValue.setFlag("order");
        } else {
            outKey.set(split[0]);
            outValue.setId("");
            outValue.setPid(split[0]);
            outValue.setAmount(0);
            outValue.setPname(split[1]);
            outValue.setFlag("pd");

        }
        context.write(outKey, outValue);
    }
}

