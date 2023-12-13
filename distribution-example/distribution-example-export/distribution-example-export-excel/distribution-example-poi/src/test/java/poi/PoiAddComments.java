package poi;

import org.apache.poi.xssf.usermodel.*;
 
import java.io.FileOutputStream;
import java.io.IOException;
 
public class PoiAddComments {
    public static void main(String[] args) throws IOException {
        // 创建工作簿对象
        XSSFWorkbook wb = new XSSFWorkbook();
        // 创建工作表对象
        XSSFSheet sheet = wb.createSheet("测试添加批注");
        // 创建绘图对象
        XSSFDrawing p = sheet.createDrawingPatriarch();
        // 创建单元格对象,批注插入到1行,1列,B5单元格
        XSSFCell cell = sheet.createRow(0).createCell(0);
        // 插入单元格内容
        cell.setCellValue(new XSSFRichTextString("批注"));
        // 获取批注对象
        // (int dx1, int dy1, int dx2, int dy2, short col1, int row1, short col2, int row2)
        // 前四个参数是坐标点,后四个参数是编辑和显示批注时的大小.
        XSSFComment comment = p.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
        // 输入批注信息
        comment.setString(new XSSFRichTextString("批注!"));
        // 添加作者,选中B5单元格,看状态栏
        comment.setAuthor("wj");
        // 将批注添加到单元格对象中
        cell.setCellComment(comment);
        // 创建输出流
        FileOutputStream out = new FileOutputStream("d:/TestAddComments.xlsx");
        wb.write(out);
        // 关闭流对象
        out.close();
    }
 
}

