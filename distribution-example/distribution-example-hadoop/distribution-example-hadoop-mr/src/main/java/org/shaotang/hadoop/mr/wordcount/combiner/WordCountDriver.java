package org.shaotang.hadoop.mr.wordcount.combiner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.shaotang.hadoop.mr.wordcount.AbstractWordCountDriver;
import org.shaotang.hadoop.mr.wordcount.WordCountMapper;
import org.shaotang.hadoop.mr.wordcount.WordCountReducer;

import java.io.IOException;

public class WordCountDriver extends AbstractWordCountDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(WordCountDriver.class);

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setCombinerClass(WordCountCombiner.class);
        FileInputFormat.setInputPaths(job, new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/input/inputword"));
        FileOutputFormat.setOutputPath(job, new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/outputwordcombine"));
        boolean res = job.waitForCompletion(true);

        System.exit(res ? 0 : 1);
    }
}
