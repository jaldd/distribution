package easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;
import easyexcel.com.example.model.ComplexHeadData;
import easyexcel.com.example.model.DemoData;
import easyexcel.com.example.model.IndexData;
import org.junit.Test;

import java.util.*;

public class TestExcel {

    private static final String FILE_TEST_PATH = "D:\\project\\";

    /**
     * 最简单的写
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 直接写即可
     */
    @org.junit.Test
    public void simpleWrite() {
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
// 根据用户传入字段 假设我们要忽略 date
        Set<String> excludeColumnFiledNames = new HashSet<String>();
        excludeColumnFiledNames.add("date");
        // 写法1 JDK8+
        // since: 3.0.0-beta1
        String fileName = FILE_TEST_PATH + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoData.class)
                .excludeColumnFiledNames(excludeColumnFiledNames)
                .sheet("模板")
                .doWrite(() -> {
                    // 分页查询数据
                    return data();
                });

        // 写法2
        fileName = FILE_TEST_PATH + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        Set<String> includeColumnFiledNames = new HashSet<String>();
        includeColumnFiledNames.add("date");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoData.class)
                .includeColumnFiledNames(includeColumnFiledNames)
                .sheet("模板").doWrite(data());

        // 写法3
        fileName = FILE_TEST_PATH + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        excelWriter.write(data(), writeSheet);
        excelWriter.finish();
    }


    /**
     * 指定写入的列
     * <p>1. 创建excel对应的实体对象 参照{@link IndexData}
     * <p>2. 使用{@link ExcelProperty}注解指定写入的列
     * <p>3. 直接写即可
     */
    @Test
    public void indexWrite() {
        String fileName = FILE_TEST_PATH + "indexWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, IndexData.class).sheet("模板").doWrite(data());
    }

    /**
     * 复杂头写入
     * <p>1. 创建excel对应的实体对象 参照{@link ComplexHeadData}
     * <p>2. 使用{@link ExcelProperty}注解指定复杂的头
     * <p>3. 直接写即可
     */
    @Test
    public void complexHeadWrite() {
        String fileName = FILE_TEST_PATH + "complexHeadWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, ComplexHeadData.class).sheet("模板").doWrite(data());
    }

    private List<DemoData> data() {
        DemoData data1 = new DemoData();
        data1.setDoubleData(1.1);
        data1.setIgnore("i1");
        data1.setString("str");
        data1.setDate(new Date());
        DemoData data2 = new DemoData();
        data2.setDoubleData(2.1);
        data2.setIgnore("i2");
        data2.setString("str2");
        data2.setDate(new Date());
        return Arrays.asList(data1, data2);
    }


}
