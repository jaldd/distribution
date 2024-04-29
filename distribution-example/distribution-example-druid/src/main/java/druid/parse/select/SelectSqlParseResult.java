package druid.parse.select;

import lombok.Data;

import java.util.List;

@Data
public class SelectSqlParseResult extends SqlParseResult {
    private List<SelectSqlParseField> fieldList;
}
