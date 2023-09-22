package poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class TestPoi {
    private static final String FILE_TEST_PATH = "D:\\project\\";


    @Test
    public void test1() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();

        FileOutputStream fileOut = new FileOutputStream(FILE_TEST_PATH + "workbook.xls");
        HSSFSheet sheet1 = wb.createSheet("new sheet");

        HSSFSheet sheet2 = wb.createSheet("second sheet");


        // Create a row and put some cells in it. Rows are 0 based.

        HSSFRow row = sheet1.createRow((short) 0);

        // Create a cell and put a value in it.

        HSSFCell cell = row.createCell((short) 0);

        cell.setCellValue(1);

        // Or do it on one line.

        HSSFCell cell1 = row.createCell((short) 1);
        cell1.setCellValue(1.2);
        HSSFCellStyle cellStyle1 = wb.createCellStyle();

        cellStyle1.setAlignment(HorizontalAlignment.LEFT);

        cell1.setCellStyle(cellStyle1);

        HSSFCell cell2 = row.createCell((short) 2);
        cell2.setCellValue("This is a string");

        HSSFCellStyle cellStyle2 = wb.createCellStyle();

        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        cellStyle2.setFillBackgroundColor(IndexedColors.RED.getIndex());

        cell2.setCellStyle(cellStyle2);

        HSSFCell cell3 = row.createCell((short) 3);
        cell3.setCellValue(true);
        HSSFCellStyle cellStyle3 = wb.createCellStyle();

        cellStyle3.setAlignment(HorizontalAlignment.RIGHT);
        cell3.setCellStyle(cellStyle3);
//        row.createCell((short) 4).setCellType(HSSFCell.);


        HSSFRow row1 = sheet1.createRow((short) 1);

        HSSFCell cell11 = row1.createCell((short) 0);
        row1.setHeight((short) 1000);
        HSSFCellStyle cellStyle11 = wb.createCellStyle();
        cell11.setCellStyle(cellStyle11);
        cell11.setCellValue("\"导入必看（本行请勿删除）：1.红色表头表示必填；2.下拉框的字典范围请勿调整，以免导入失败；\n" +
                "3.关联标准集列一定要按照sheet\"\"标准集清单\"\"填写，关联多个标准集时，英文分号;间隔，举例：主体/法人信息;主体/自然人信息;主体/企业\"\t\t\t\t\t\t\t\t\t\t\t\t");
        sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, 12));


        HSSFRow row2 = sheet1.createRow((short) 2);

        HSSFCell cell21 = row2.createCell((short) 0);
        row2.setHeightInPoints(50);
        HSSFCellStyle cellStyle21 = wb.createCellStyle();
        cell21.setCellStyle(cellStyle21);

        String str="导入必看（本行请勿删除）：1.红色表头表示必填；2.下拉框的字典范围请勿调整，以免导入失败；\n" +
                "3.关联标准集列一定要按照sheet\"\"标准集清单\"\"填写，关联多个标准集时，英文分号;间隔，举例：主体/法人信息;主体/自然人信息;主体/企业\t\t\t\t\t\t\t\t\t\t\t\t";
        RichTextString richTextString = new HSSFRichTextString(str);
        Font writeFont = wb.createFont();
        writeFont.setFontHeightInPoints((short)11);
        writeFont.setColor(IndexedColors.RED.getIndex());
        writeFont.setFontName("宋体");
        richTextString.applyFont(0, 12, writeFont);
        Font writeFont1 = wb.createFont();
        writeFont1.setFontHeightInPoints((short)11);
        writeFont1.setColor(IndexedColors.BLACK.getIndex());
        writeFont1.setFontName("宋体");
        richTextString.applyFont(13, str.length(), writeFont1);
        cell21.setCellValue(richTextString);
//        cell21.setCellValue("\"导入必看（本行请勿删除）：1.红色表头表示必填；2.下拉框的字典范围请勿调整，以免导入失败；\n" +
//                "3.关联标准集列一定要按照sheet\"\"标准集清单\"\"填写，关联多个标准集时，英文分号;间隔，举例：主体/法人信息;主体/自然人信息;主体/企业\"\t\t\t\t\t\t\t\t\t\t\t\t");
        sheet1.addMergedRegion(new CellRangeAddress(2, 2, 0, 12));



        HSSFRow row3 = sheet1.createRow((short) 3);
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(3, 3, 2, 2);
        DataValidationHelper dataValidationHelper = sheet1.getDataValidationHelper();
        DataValidationConstraint constraint = dataValidationHelper.createExplicitListConstraint(new String[] {"测试1", "测试2"});
        DataValidation dataValidation = dataValidationHelper.createValidation(constraint, cellRangeAddressList);
        sheet1.addValidationData(dataValidation);
        HSSFCell cell31 = row3.createCell((short) 2);
        cell31.setCellValue("abcde");






        wb.write(fileOut);


        fileOut.close();

    }

}
