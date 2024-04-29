package druid.parse.select;

import lombok.Data;

@Data
public class SelectSqlParseField {
    private String fieldName;

    private String fieldEnName;

    private String fieldType;
    private Integer fieldLength;

    private Integer fieldDecimals;
}
