package org.shaotang.hadoop.mr.mapperJoin;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class TableReducer extends Reducer<Text, TableBean, TableBean, NullWritable> {

    private final IntWritable outV = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<TableBean> values, Reducer<Text, TableBean, TableBean, NullWritable>.Context context) throws IOException, InterruptedException {

        List<TableBean> list = new ArrayList<>();
        TableBean pd = new TableBean();

        for (TableBean value : values) {
            if ("order".equals(value.getFlag())) {
                TableBean temp = new TableBean();
                try {
                    BeanUtils.copyProperties(temp, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                list.add(temp);
            } else {

                try {
                    BeanUtils.copyProperties(pd, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        for (TableBean tableBean : list) {
            tableBean.setPname(pd.getPname());
            context.write(tableBean, NullWritable.get());
        }
    }
}
