package org.shaotang.hadoop.mr.writeable.comparable.partitioner;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class ProvincePartitioner extends Partitioner<FlowBean, Text> {

    @Override
    public int getPartition(FlowBean flowBean, Text text, int numPartitions) {
        String phone = text.toString();
        String pre = phone.substring(0, 3);
        int partition;
        if ("136".equals(pre)) {
            partition = 0;
        } else if ("137".equals(pre)) {
            partition = 1;
        } else if ("138".equals(pre)) {
            partition = 2;
        } else if ("139".equals(pre)) {
            partition = 3;
        } else {
            partition = 4;
        }
        return partition ;
    }
}
