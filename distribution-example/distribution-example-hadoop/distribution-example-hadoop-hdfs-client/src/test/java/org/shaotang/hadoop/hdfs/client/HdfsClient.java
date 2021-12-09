package org.shaotang.hadoop.hdfs.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class HdfsClient {

    private FileSystem fs;

    @Before
    public void init() throws IOException, InterruptedException, URISyntaxException {
        URI uri=new URI("hdfs://hadoop-pseudo:8020");

        Configuration config=new Configuration();
        fs = FileSystem.get(uri, config,"root");

    }

    @After
    public void close() throws IOException {
        fs.close();
    }
    
    @Test
    public void testMkdir() throws URISyntaxException, IOException, InterruptedException {


        fs.mkdirs(new Path("/xiyou/tiantang"));


    }


    public static void main(String[] args) {
        System.out.println(1);
    }
}