package org.shaotang.hadoop.mr.partitioner;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.shaotang.hadoop.mr.wordcount.AbstractWordCountDriver;

import java.io.IOException;

public class WordCountDriver extends AbstractWordCountDriver {


    public boolean execute(String inputPath, String outputPath) throws IOException, InterruptedException, ClassNotFoundException {

        Job job = getJob();
        job.setNumReduceTasks(2);
        FileInputFormat.setInputPaths(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        return job.waitForCompletion(true);
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {


        boolean res = new WordCountDriver().execute(
                "distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/input/inputword",
                "distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/outputword2");
        System.exit(res ? 0 : 1);
    }
}
