package org.shaotang.hadoop.mr.combineTextInputformat;

import org.shaotang.hadoop.mr.wordcount.AbstractWordCountDriver;

import java.io.IOException;

public class WordCountDriver extends AbstractWordCountDriver {


//    public boolean execute(String inputPath, String outputPath) throws IOException, InterruptedException, ClassNotFoundException {
//
//        Job job = getJob();
//        job.setInputFormatClass(CombineTextInputFormat.class);
//        CombineTextInputFormat.setMaxInputSplitSize(job, 4194304);
//        FileInputFormat.setInputPaths(job, new Path(inputPath));
//        FileOutputFormat.setOutputPath(job, new Path(outputPath));
//        return job.waitForCompletion(true);
//    }


    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {


        boolean res = new WordCountDriver().execute(
                "distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/input/inputsmall",
                "distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/combine1");
        System.exit(res ? 0 : 1);
    }
}
