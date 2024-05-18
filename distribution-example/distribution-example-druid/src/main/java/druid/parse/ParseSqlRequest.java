package druid.parse;

import lombok.Data;

@Data
public class ParseSqlRequest {
    private Integer dataSourceType;

    private String sql;
}