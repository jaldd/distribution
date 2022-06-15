package sas;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epam.parso.Column;
import com.epam.parso.SasFileReader;
import com.epam.parso.impl.SasFileReaderImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestSas {

    public static void main(String[] args) {
        JSONArray array = new TestSas().readSas(new File("C:\\Users\\lidd-h\\Downloads\\formats_cfps_chinese.sas7bcat"), "formats_cfps_chinese.sas7bcat");
        System.out.println(array.toJSONString());
    }


    /**
     * @param file 需要解析的Sas文件
     *             这里获取数据
     *             json数据格式 文件名.列名type：data  type为数据类型，自定义
     */
    public JSONArray readSas(File file, String filenames) {
        //获取数据集合
        List<Object[]> list = new ArrayList<>();
        //获取头部行数据
        List<String> header = new ArrayList<>();
        InputStream is = null;
        try {
            is = new FileInputStream(file);//将文件输入到流解析
            //创建SasFileReader类的变量，并指示包含SAS7BDAT文件的InputStream作为
            //SasFileReader构造函数中的参数
            SasFileReader sasFileReader = new SasFileReaderImpl(is);
            //获取SAS7BDAT文件的属性
            sasFileReader.getSasFileProperties();
            //获得SAS7BDAT列的描述
            List<Column> next = sasFileReader.getColumns();
            //根据列类型判断使用什么方法解析数据
            for (Column temp : next) {
                String type = temp.getType().toString();
                if ("Number".equals(type.substring(type.lastIndexOf(".") + 1))) {
                    header.add(temp.getName() + "01");
                } else {
                    header.add(temp.getName() + "00");
                }
            }
            int size = header.size();
            String headName = null;
            String type = null;
            //判断列名是否包含特殊字符，包含则自定义列名 第n列
            for (int i = 0; i < size; i++) {
                headName = header.get(i).substring(0, header.get(i).length() - 2).trim();
                if (headName.length() > 0) {
                    if (!isLetterDigitOrChinese(headName.substring(0, 1))) {
                        type = header.get(i).substring(header.get(i).length() - 2);
                        header.set(i, "第" + (i + 1) + "列" + type);
                    }
                } else {
                    type = header.get(i).substring(header.get(i).length() - 2);
                    header.set(i, "第" + (i + 1) + "列" + type);
                }
            }
            Object[] data = null;
            //获取所有数据
            while ((data = sasFileReader.readNext()) != null) {
                //添加数据集合
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
                log.trace("InputStream closure error");
            }
        }
        return readSasContent(list, header, filenames);
    }


    /**
     * 将读取出来的数据转换为Json
     * 这里定义json添加的key形式和data数据处理
     *
     * @param list     数据集合
     * @param header   列名
     * @param fileName 文件名
     * @return
     */
    public JSONArray readSasContent(List<Object[]> list, List<String> header,
                                    String fileName) {
        JSONArray resJson = new JSONArray();
        JSONObject jsonObj = null;

        int size = list.size();
        int cloumnSize = header.size();
        //循环处理每条数据
        for (int i = 0; i < size; i++) {
            jsonObj = new JSONObject(true);
            Object[] cellValue = list.get(i);
            //System.out.print(cellValue.length);
            for (int j = 0; j < cloumnSize; j++) {
                jsonObj.put(fileName + "." + header.get(j), cellValue[j]);
            }
            //System.out.println(jsonObj.toJSONString()); 打印数据，检查
            resJson.add(jsonObj);
        }
        //System.out.println(list.size());
        //System.out.println(resJson.size());
        //下面四行个人需要，看业务来
        JSONObject sasJson = new JSONObject();
        sasJson.put(fileName, resJson);
        JSONArray sasArr = new JSONArray();
        sasArr.add(sasJson);

        return sasArr;
    }

    /**
     * 判断是否包含字母或数字或中文  用于生成列名
     *
     * @param str
     * @return
     */
    public boolean isLetterDigitOrChinese(String str) {
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";//其他需要，直接修改正则表达式就好
        return str.matches(regex);
    }
}
