package org.shaotang.hadoop.hdfs.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@Slf4j
public class HdfsClient {

    private FileSystem fs;

    @Before
    public void init() throws IOException, InterruptedException, URISyntaxException {
        URI uri = new URI("hdfs://hadoop-pseudo:8020");

        Configuration config = new Configuration();
        config.set("dfs.replication", "2");
        fs = FileSystem.get(uri, config, "root");

    }

    @After
    public void close() throws IOException {
        fs.close();
    }

    @Test
    public void testMkdir() throws URISyntaxException, IOException, InterruptedException {


        fs.mkdirs(new Path("/xiyou/huaguoshan"));


    }

    @Test
    public void testPutFile() throws IOException {
        fs.copyFromLocalFile(false, true,
                new Path("/usr/local/hadoop/hadoop-3.1.4/test/wukong.txt"),
                new Path("/xiyou/huaguoshan"));
    }

    @Test
    public void testGetFile() throws IOException {
        fs.copyToLocalFile(false, new Path("/xiyou/huaguoshan/wukong.txt")
                , new Path("/usr/local/hadoop/hadoop-3.1.4/test/sunwukong2.txt"), false);
    }

    @Test
    public void testRm() throws IOException {
        fs.delete(new Path("/xiyou/huaguoshan"), true);
    }

    @Test
    public void testMv() throws IOException {
        fs.rename(new Path("/xiyou/huaguoshan/wukong.txt"), new Path("/xiyou/huaguoshan/liuer.txt"));
    }

    @Test
    public void testFileDetail() throws IOException {
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
        while (listFiles.hasNext()) {
            LocatedFileStatus fileStatus = listFiles.next();
            System.out.println(fileStatus.getPath());
            System.out.println(fileStatus.getPath().getName());
            System.out.println(fileStatus.isFile());
            BlockLocation[] locations = fileStatus.getBlockLocations();
            System.out.println(Arrays.asList(locations));
        }

        FileStatus[] fileStatuses = fs.listStatus(new Path("/xiyou/huaguoshan"));
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println(fileStatus.getPath().getName() + ":" + fileStatus.isFile());
        }
    }

    public static void main(String[] args) {
        System.out.println(1);
    }
}