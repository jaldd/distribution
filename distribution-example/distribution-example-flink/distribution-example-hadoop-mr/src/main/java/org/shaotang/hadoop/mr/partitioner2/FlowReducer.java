package org.shaotang.hadoop.mr.partitioner2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowReducer extends Reducer<Text, FlowBean, Text, FlowBean> {

    private FlowBean outV=new FlowBean();
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Reducer<Text, FlowBean, Text, FlowBean>.Context context) throws IOException, InterruptedException {

        long totalUp=0;
        long totalDown=0;
        for(FlowBean bean:values){
            totalUp+=bean.getUpFlow();
            totalDown=bean.getDownFlow();
        }
        outV.setUpFlow(totalUp);
        outV.setDownFlow(totalDown);

        context.write(key,outV);
    }
}
