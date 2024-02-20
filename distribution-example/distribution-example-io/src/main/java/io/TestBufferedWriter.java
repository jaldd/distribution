package io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class TestBufferedWriter {

    public static void main(String[] args) throws IOException {
        Writer output = new BufferedWriter(new FileWriter("D:\\bb.txt"));
        output.append("New Line!");
        output.append("abc");
        output.close();
    }
}
