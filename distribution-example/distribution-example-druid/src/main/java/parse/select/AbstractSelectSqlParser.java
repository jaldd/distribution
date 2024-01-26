package parse.select;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
public abstract class AbstractSelectSqlParser implements SelectSqlParser {
    protected String sql;
    protected Integer dataSourceType;
    protected SQLSelectStatement sqlSelectStatement;

    public AbstractSelectSqlParser(String sql, Integer dataSourceType) {
        this.sql = sql;
        this.dataSourceType = dataSourceType;
        this.sqlSelectStatement = this.createSqlSelectStatement();
    }

    @Override
    public SelectSqlParseResult parseSelectSql() {
        SelectSqlParseResult selectSqlParseResult = new SelectSqlParseResult();
        List<SelectSqlParseField> fieldVOList = this.parseSelectFields();
        selectSqlParseResult.setFieldList(fieldVOList);
        return selectSqlParseResult;
    }

    /**
     * 创建SqlSelectStatement
     *
     * @return
     */
    private SQLSelectStatement createSqlSelectStatement() {
        SQLStatement sqlStatement = this.createSqlStatement();
        return createSqlSelectStatement(sqlStatement);
    }

    /**
     * 获取SqlStatement
     *
     * @return
     */
    private SQLStatement createSqlStatement() {
        SQLStatementParser parser;
        List<SQLStatement> stmtList = new ArrayList<>();
        String noCommentSql = this.removeComment(sql);
        try {
            parser = SQLParserUtils.createSQLStatementParser(noCommentSql, this.getDbType());
            stmtList = parser.parseStatementList();
        } catch (Exception e) {
            log.error("查询语句解析失败,sql={}", sql, e);
        }
        if (stmtList.isEmpty()) {
            log.error("查询语句解析失败!");
        } else if (stmtList.size() > 1) {
            log.error("查询语句解析失败,不支持多条SQL语句,当前是" + stmtList.size() + "条语句");
        }

        boolean existsInto = Pattern.compile("into", Pattern.CASE_INSENSITIVE).matcher(noCommentSql).find();
        if (existsInto) {
            log.error("查询语句解析失败,不支持INTO语法");
        }

        return stmtList.get(0);
    }

    protected DbType getDbType() {
        throw new IllegalArgumentException("un supported");
    }

    protected SQLSelectStatement createSqlSelectStatement(SQLStatement sqlStatement) {
        if (sqlStatement instanceof SQLSelectStatement) {
            return (SQLSelectStatement) sqlStatement;
        } else {
            throw new IllegalArgumentException("un support");
        }
    }

    protected abstract List<SelectSqlParseField> parseSelectFields();

    protected String removeComment(String sql) {
        // 移除单行注释 -- | #开头
        String pattern = "\\s*(#|--)(.*)(\r|\n)*";
        Pattern p = Pattern.compile(pattern);
        String replaceSql = sql.replace("`#`", "@@");
        Matcher m = p.matcher(replaceSql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, " ");
        }
        m.appendTail(sb);
        String targetSql = sb.toString().replace("@@", "#");
        return targetSql;
    }

    public static void main(String[] args) {
//        String sql = " aa \nselect*  ";
//        String pattern = ".*SELECT\\s*\\*.*";
//        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(sql);
//        int i = 0;
//        while (m.find()) {
//            System.out.println(i++ + "不支持 SELECT * 语法");
//        }
        String sql = " select\n* 2 ";
        String patternStr = "(.|\\s)*SELECT\\s\\*.*";

        Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
        }
    }
}
