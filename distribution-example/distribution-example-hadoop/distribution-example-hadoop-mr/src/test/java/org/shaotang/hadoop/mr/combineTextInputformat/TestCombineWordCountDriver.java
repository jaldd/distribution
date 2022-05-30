package org.shaotang.hadoop.mr.combineTextInputformat;

import org.junit.Test;
import org.shaotang.hadoop.mr.wordcount.WordCountDriver;

import java.io.IOException;

public class TestCombineWordCountDriver {

    @Test
    public void testExecute() throws IOException, InterruptedException, ClassNotFoundException {
        WordCountDriver wordCountDriver = new WordCountDriver();
        wordCountDriver.execute("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/input/inputsmall",
                "distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/combine1");
    }
}
