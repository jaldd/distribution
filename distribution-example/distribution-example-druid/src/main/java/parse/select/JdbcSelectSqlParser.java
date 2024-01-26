package parse.select;

public abstract class JdbcSelectSqlParser extends AbstractSelectSqlParser {

    public JdbcSelectSqlParser(String sql, Integer dataSourceType) {
        super(sql, dataSourceType);
    }

}
