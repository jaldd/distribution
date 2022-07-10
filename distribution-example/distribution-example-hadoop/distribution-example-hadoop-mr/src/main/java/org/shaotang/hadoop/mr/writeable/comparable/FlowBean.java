package org.shaotang.hadoop.mr.writeable.comparable;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FlowBean implements WritableComparable<FlowBean> {

    private long upFlow;
    private long downFlow;
    private long sumFlow;

    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public long getSumFlow() {
        return upFlow + downFlow;
    }

    public void setSumFlow() {
        this.sumFlow = upFlow + downFlow;;
    }

    public FlowBean() {
    }

    @Override
    public void write(DataOutput out) throws IOException {

        out.writeLong(upFlow);
        out.writeLong(downFlow);
        out.writeLong(sumFlow);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.upFlow = in.readLong();
        this.downFlow = in.readLong();
        this.sumFlow = in.readLong();
    }

    @Override
    public String toString() {
//        return "FlowBean{" +
//                "upFlow=" + upFlow +
//                ", downFlow=" + downFlow +
//                ", sumFlow=" + sumFlow +
//                '}';
        return upFlow + "\t" + downFlow + '\t' + getSumFlow();
    }

    @Override
    public int compareTo(FlowBean o) {

        if (this.sumFlow > o.sumFlow) {
            return -1;
        } else if (this.sumFlow < o.sumFlow) {
            return 1;
        }

        if (this.upFlow > o.upFlow) {
            return 1;
        } else if (this.upFlow < o.upFlow) {
            return - 1;
        }
        return 0;
    }
}
