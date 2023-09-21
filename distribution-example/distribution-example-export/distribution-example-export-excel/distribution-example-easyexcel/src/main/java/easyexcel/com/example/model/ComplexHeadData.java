package easyexcel.com.example.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class ComplexHeadData {
    @ExcelProperty(value = {"主标题", "字符串标题"}, index = 0)
    private String string;
    @ExcelProperty(value = {"主标题", "日期标题"}, index = 1)
    private Date date;
    @ExcelProperty(value = {"主标题", "数字标题"}, index = 3)
    private Double doubleData;
}