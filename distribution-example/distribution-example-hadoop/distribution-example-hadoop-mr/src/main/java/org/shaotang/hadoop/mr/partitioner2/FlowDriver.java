package org.shaotang.hadoop.mr.partitioner2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Configuration conf = new Configuration();
        Job job= Job.getInstance(conf);
        job.setJarByClass(FlowDriver.class);

        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);

        job.setPartitionerClass(ProvincePartitioner.class);
        job.setNumReduceTasks(5);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        FileInputFormat.setInputPaths(job,new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/input/inputflow"));
        FileOutputFormat.setOutputPath(job,new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/outputflow2"));

        boolean res = job.waitForCompletion(true);

        System.exit(res ? 0 : 1);
    }
}
