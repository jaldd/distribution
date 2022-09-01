package org.shaotang.hadoop.mr.etl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.shaotang.hadoop.mr.wordcount.AbstractWordCountDriver;

import java.io.IOException;
import java.net.URISyntaxException;

public class WeblogDriver extends AbstractWordCountDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, URISyntaxException {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(WeblogDriver.class);

        job.setMapperClass(WebLogMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);


        job.setNumReduceTasks(0);

        FileInputFormat.setInputPaths(job, new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/input/inputlog"));
        FileOutputFormat.setOutputPath(job, new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/outputlog"));
        boolean res = job.waitForCompletion(true);

        System.exit(res ? 0 : 1);
    }
}
