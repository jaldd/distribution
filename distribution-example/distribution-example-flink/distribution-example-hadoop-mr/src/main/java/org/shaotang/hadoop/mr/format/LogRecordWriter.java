package org.shaotang.hadoop.mr.format;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

public class LogRecordWriter extends RecordWriter<Text, NullWritable> {

    FSDataOutputStream baidu;
    FSDataOutputStream other;

    public LogRecordWriter(TaskAttemptContext job) {

        try {
            FileSystem fileSystem = FileSystem.get(job.getConfiguration());
            baidu = fileSystem.create(new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/outputformat/baidu.log"));
            other = fileSystem.create(new Path("distribution-example/distribution-example-hadoop/distribution-example-hadoop-mr/src/main/resources/output/outputformat/other.log"));
        } catch (Exception e) {

        }
    }

    @Override
    public void write(Text key, NullWritable value) throws IOException, InterruptedException {

        String log = key.toString();
        if (log.contains("baidu")) {
            baidu.writeBytes(log + "\n");
        } else {
            other.writeBytes(log + "\n");
        }
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {

        IOUtils.closeStream(baidu);
        IOUtils.closeStream(other);
    }
}
