package org.shaotang.hadoop.mr.wordcount;

import java.io.IOException;

public class WordCountDriver extends AbstractWordCountDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

//        Configuration conf = new Configuration();
//        Job job = Job.getInstance(conf);
//
//        job.setJarByClass(WordCountDriver.class);
//
//        job.setMapperClass(WordCountMapper.class);
//        job.setReducerClass(WordCountReducer.class);
//
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(IntWritable.class);
//
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(IntWritable.class);
//        FileInputFormat.setInputPaths(job, new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/input/inputword"));
//        FileOutputFormat.setOutputPath(job, new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/outputword"));
//        boolean res = job.waitForCompletion(true);

        boolean res = new WordCountDriver().execute("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/input/inputword",
                "distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/outputword");
        System.exit(res ? 0 : 1);
    }
}
