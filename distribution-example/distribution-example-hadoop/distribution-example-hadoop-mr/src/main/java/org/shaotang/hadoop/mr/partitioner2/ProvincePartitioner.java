package org.shaotang.hadoop.mr.partitioner2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class ProvincePartitioner extends Partitioner<Text, FlowBean> {

    @Override
    public int getPartition(Text text, FlowBean flowBean, int numPartitions) {
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
