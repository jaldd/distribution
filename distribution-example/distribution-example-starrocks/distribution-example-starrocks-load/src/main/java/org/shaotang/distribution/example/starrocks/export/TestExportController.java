package org.shaotang.distribution.example.starrocks.export;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
public class TestExportController {


    @RequestMapping("test/export")
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = "测试";

        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        ServletOutputStream os = response.getOutputStream();

        ExcelWriterBuilder writerBuilder = EasyExcel.write(os).registerWriteHandler(new ExportServiceImpl().getHorizontalCellStyleStrategy());

        ExcelWriter writer = writerBuilder.build();
        WriteSheet sourceSheet = EasyExcel.writerSheet(0).sheetName("sheet").build();
        List<List<String>> sourceHeaderList = new ArrayList<>();
        sourceHeaderList.add(Collections.singletonList("12345"));
        sourceSheet.setHead(sourceHeaderList);
        for (int i = 0; i < 10; i++) {
            List<List<Object>> sourceExcelDatas = new ArrayList<>();
            sourceExcelDatas.add(Collections.singletonList("上山打老虎" + i));
            Thread.sleep(10);
            writer.write(sourceExcelDatas, sourceSheet);
        }

        writer.finish();
        for (int i = 0; i < 10; i++) {
            List<List<Object>> sourceExcelDatas = new ArrayList<>();

            sourceExcelDatas.add(Collections.singletonList("上山打老虎" + i + "-" + i));
            Thread.sleep(10);
            writer.write(sourceExcelDatas, sourceSheet);
        }

        writer.finish();
    }

    @RequestMapping("test/export1")
    public void test1(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");

        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        ServletOutputStream os = response.getOutputStream();

        ExcelWriterBuilder writerBuilder = EasyExcel.write(os).registerWriteHandler(new ExportServiceImpl().getHorizontalCellStyleStrategy());

        ExcelWriter writer = writerBuilder.build();
        WriteSheet sourceSheet = EasyExcel.writerSheet(0).sheetName("sheet").build();
        List<List<String>> sourceHeaderList = new ArrayList<>();
        sourceHeaderList.add(Collections.singletonList("12345"));
        sourceSheet.setHead(sourceHeaderList);
        for (int i = 0; i < 10; i++) {
            List<List<Object>> sourceExcelDatas = new ArrayList<>();
            sourceExcelDatas.add(Collections.singletonList("上山打老虎" + i));
            Thread.sleep(10);
            writer.write(sourceExcelDatas, sourceSheet);
        }

        writer.finish();
        for (int i = 0; i < 10; i++) {
            List<List<Object>> sourceExcelDatas = new ArrayList<>();

            sourceExcelDatas.add(Collections.singletonList("上山打老虎" + i + "-" + i));
            Thread.sleep(10);
            writer.write(sourceExcelDatas, sourceSheet);
        }

        writer.finish();
    }

    private ExecutorService executor;

    @PostConstruct
    public void open() {
        executor = Executors.newCachedThreadPool();
    }

    @RequestMapping("test/export2")
    public void test2(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");

        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        ServletOutputStream os = response.getOutputStream();


        try (PipedOutputStream pos = new PipedOutputStream();
             PipedInputStream pin = new PipedInputStream(pos)) {
            log.info("exportToObs before submit");

            executor.submit(() -> {
                try {

                    ExcelWriterBuilder writerBuilder = EasyExcel.write(pos).registerWriteHandler(new ExportServiceImpl().getHorizontalCellStyleStrategy());

                    ExcelWriter writer = writerBuilder.build();
                    WriteSheet sourceSheet = EasyExcel.writerSheet(0).sheetName("sheet").build();
                    List<List<String>> sourceHeaderList = new ArrayList<>();
                    sourceHeaderList.add(Collections.singletonList("12345"));
                    sourceSheet.setHead(sourceHeaderList);

                    List<List<Object>> sourceExcelDatas = new ArrayList<>();

                    for (int i1 = 0; i1 < 100; i1++) {

                        for (int i = 0; i < 100; i++) {
                            sourceExcelDatas.add(Collections.singletonList("上山打老虎" + i1 + "-" + i));
                            Thread.sleep(10);
                        }
                        writer.write(sourceExcelDatas, sourceSheet);
                    }
                    writer.finish();

                } catch (Exception e) {
                    log.error("executor error:" + e.getMessage(), e);
                }
            });

            log.info("executor executed");
            StreamUtils.copy(pin, os);
        }


//        ExcelWriterBuilder writerBuilder = EasyExcel.write(os).registerWriteHandler(new ExportServiceImpl().getHorizontalCellStyleStrategy());
//
//        ExcelWriter writer = writerBuilder.build();
//        WriteSheet sourceSheet = EasyExcel.writerSheet(0).sheetName("sheet").build();
//        List<List<String>> sourceHeaderList = new ArrayList<>();
//        sourceHeaderList.add(Collections.singletonList("12345"));
//        sourceSheet.setHead(sourceHeaderList);
//        for (int i = 0; i < 10; i++) {
//            List<List<Object>> sourceExcelDatas = new ArrayList<>();
//            sourceExcelDatas.add(Collections.singletonList("上山打老虎" + i));
//            Thread.sleep(10);
//            writer.write(sourceExcelDatas, sourceSheet);
//        }
//
//        writer.finish();
//        for (int i = 0; i < 10; i++) {
//            List<List<Object>> sourceExcelDatas = new ArrayList<>();
//
//            sourceExcelDatas.add(Collections.singletonList("上山打老虎" + i + "-" + i));
//            Thread.sleep(10);
//            writer.write(sourceExcelDatas, sourceSheet);
//        }
//
//        writer.finish();
    }

}