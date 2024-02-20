package io;

import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestIOUtils {
    public static void main(String[] args) throws FileNotFoundException, IOException {
//		System.out.println();
        //写数据
        IOUtils.write("aa1",
                new FileOutputStream("D:\\bb.txt"));
        IOUtils.write("aa2",
                new FileOutputStream("D:\\bb.txt"));
        IOUtils.write("aa3",
                new FileOutputStream("D:\\bb.txt"));
        IOUtils.write("aa4",
                new FileOutputStream("D:\\bb.txt"));
        IOUtils.write("aa5",
                new FileOutputStream("D:\\bb.txt"));
//        IOUtils.writeLines(Arrays.asList("aaa", "aaa1"), IOUtils.LINE_SEPARATOR_WINDOWS,
//                new FileOutputStream("D:\\bb.txt"));


    }
}
