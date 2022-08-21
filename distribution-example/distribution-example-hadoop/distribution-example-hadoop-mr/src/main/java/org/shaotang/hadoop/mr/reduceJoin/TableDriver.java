package org.shaotang.hadoop.mr.reduceJoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.shaotang.hadoop.mr.wordcount.AbstractWordCountDriver;

import java.io.IOException;

public class TableDriver extends AbstractWordCountDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(TableDriver.class);

        job.setMapperClass(TableMapper.class);
        job.setReducerClass(TableReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(TableBean.class);

        job.setOutputKeyClass(TableBean.class);
        job.setOutputValueClass(NullWritable.class);
        FileInputFormat.setInputPaths(job, new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/input/inputtable"));
        FileOutputFormat.setOutputPath(job, new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/outputtable"));
        boolean res = job.waitForCompletion(true);

        System.exit(res ? 0 : 1);
    }
}
