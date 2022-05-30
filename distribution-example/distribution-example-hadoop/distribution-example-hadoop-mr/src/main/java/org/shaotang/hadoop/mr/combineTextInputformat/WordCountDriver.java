package org.shaotang.hadoop.mr.combineTextInputformat;

import org.shaotang.hadoop.mr.wordcount.AbstractWordCountDriver;

import java.io.IOException;

public class WordCountDriver extends AbstractWordCountDriver {


    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {


        boolean res = new WordCountDriver().execute(
                "distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/input/inputsmall",
                "distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/combine1");
        System.exit(res ? 0 : 1);
    }
}
