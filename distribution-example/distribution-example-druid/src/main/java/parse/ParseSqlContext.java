package parse;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import lombok.extern.slf4j.Slf4j;
import parse.select.MysqlSelectSqlParser;
import parse.select.SelectSqlParseResult;
import parse.select.SelectSqlParser;
import parse.select.SqlParseResult;

@Slf4j
public class ParseSqlContext {
    /**
     * 解析创建Sql
     *
     * @param sql
     * @param dbType
     * @return
     */
    public static SqlParseResult parseCreateSql(String sql, DbType dbType) {
        SQLStatement sqlStatement = getSqlStatement(sql, dbType);

        switch (dbType) {
            case mysql:
            case postgresql:
            case hive:
            case starrocks:
            default:
                break;
        }
        return null;
    }

    private static SQLStatement getSqlStatement(String sql, DbType dbType) {
        try {
            SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, true);
            return parser.parseStatement();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private static String getTableComment(String sql) {
        SQLStatement sqlStatement = getSqlStatement(sql, DbType.mysql);
        if (sqlStatement instanceof MySqlCreateTableStatement) {
            MySqlCreateTableStatement mySqlCreateTableStatement = (MySqlCreateTableStatement) sqlStatement;
            //todo

        } else {
        }
        return null;
    }

    /**
     * 解析查询Sql
     *
     * @param parseSqlRequest
     * @return
     */
    public static SelectSqlParseResult parseSelectSql(ParseSqlRequest parseSqlRequest) {
        SelectSqlParser selectSqlParser;
        switch (parseSqlRequest.getDataSourceType()) {
            case 1:
                selectSqlParser = new MysqlSelectSqlParser(parseSqlRequest.getSql(), parseSqlRequest.getDataSourceType());
                return selectSqlParser.parseSelectSql();
            default:
                break;
        }
        return null;
    }
}
