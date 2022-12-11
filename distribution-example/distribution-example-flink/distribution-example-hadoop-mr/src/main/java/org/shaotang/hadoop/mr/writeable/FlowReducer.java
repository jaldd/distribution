package org.shaotang.hadoop.mr.writeable;

import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.io.Text;
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
