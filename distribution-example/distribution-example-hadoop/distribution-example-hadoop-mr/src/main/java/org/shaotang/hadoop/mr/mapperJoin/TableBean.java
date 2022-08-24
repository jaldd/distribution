package org.shaotang.hadoop.mr.mapperJoin;

import lombok.Data;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
public class TableBean implements Writable {

    private String id;
    private String pid;
    private int amount;
    private String pname;
    private String flag;

    @Override
    public void write(DataOutput out) throws IOException {

        out.writeUTF(id);
        out.writeUTF(pid);
        out.writeInt(amount);
        out.writeUTF(pname);
        out.writeUTF(flag);
    }

    @Override
    public void readFields(DataInput in) throws IOException {

        this.id = in.readUTF();
        this.pid = in.readUTF();
        this.amount = in.readInt();
        this.pname = in.readUTF();
        this.flag = in.readUTF();
    }

    @Override
    public String toString() {
        return id + '\t' + pname + '\t' + amount;
    }
}
