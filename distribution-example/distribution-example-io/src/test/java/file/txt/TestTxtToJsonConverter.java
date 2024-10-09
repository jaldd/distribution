package file.txt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestTxtToJsonConverter {

    public static List<String> readRecordsFromTxt(String filePath) throws IOException {
        List<String> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line);
//                String[] fields = line.split(",");
//                if (fields.length >= 2) { // 确保有足够的字段
//                    Record record = new Record(fields[0], fields[1]);
//                    records.add(record);
//                }
            }
        }
        return records;
    }

    public static void main(String[] args) throws IOException {
        String filename = "D:\\tm.txt";
        System.out.println(readRecordsFromTxt(filename));
    }
}