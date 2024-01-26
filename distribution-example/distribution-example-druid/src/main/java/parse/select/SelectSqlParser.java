package parse.select;

public interface SelectSqlParser {

    /**
     * 解析查询SQL语句
     *
     * @return
     */
    SelectSqlParseResult parseSelectSql();
}
