package org.shaotang.hadoop.hdfs.client;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HdfsClient {



    @Test
    public void testMkdir() throws URISyntaxException, IOException, InterruptedException {
        URI uri=new URI("hdfs://hadoop-pseudo:8020");

        Configuration config=new Configuration();
        FileSystem  fs = FileSystem.get(uri, config,"root");

        fs.mkdirs(new Path("/xiyou/huaguoshan"));

        fs.close();
    }


    public static void main(String[] args) {
        System.out.println(1);
    }
}
