package druid.parse.select;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.excel.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MysqlSelectSqlParser extends JdbcSelectSqlParser {

    public MysqlSelectSqlParser(String sql, Integer dataSourceType) {
        super(sql, dataSourceType);
    }

    @Override
    protected DbType getDbType() {
        return DbType.mysql;
    }

    @Override
    protected List<SelectSqlParseField> parseSelectFields() {
        List<SelectSqlParseField> fieldVOList = new ArrayList<>();
        //查询列 SELECT x , t1.x AS x1, t2.a, sum(t1.a + T1.b) AS a111 , (select x from b) b1, t1.b b111, t1.c FROM (Select 1 from t) t1)
        SQLSelectQuery selectQuery = super.sqlSelectStatement.getSelect().getQuery();
        if (selectQuery instanceof SQLUnionQuery) {
            // 联合查询语句
            SQLUnionQuery sqlUnionQuery = (SQLUnionQuery) selectQuery;
            List<SQLSelectQuery> relations = sqlUnionQuery.getRelations();
            for (SQLSelectQuery relationSelectQuery : relations) {
                if (relationSelectQuery instanceof MySqlSelectQueryBlock
                        || relationSelectQuery instanceof SQLSelectQueryBlock ) {
                    fieldVOList = getFields(relationSelectQuery);
                    break;
                }
            }
        } else if (selectQuery instanceof MySqlSelectQueryBlock || selectQuery instanceof SQLSelectQueryBlock) {
            // 简单查询语句
            fieldVOList = getFields(selectQuery);
        }
        // 解析字段注释
        this.parseFieldComment(fieldVOList);
        return fieldVOList;
    }

    protected List<SelectSqlParseField> getFields(SQLSelectQuery selectQuery) {
        List<SelectSqlParseField> fieldVOList = new ArrayList<>();
        List<SQLSelectItem> list = ((SQLSelectQueryBlock) selectQuery).getSelectList();
        for (SQLSelectItem item : list) {
            SelectSqlParseField fieldVO = new SelectSqlParseField();
            String aliasField = item.getAlias();
            Object obj = item.getExpr();
            if (obj instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr expr = (SQLIdentifierExpr) obj;
                if (aliasField != null) {
                    fieldVO.setFieldEnName(aliasField);
                } else {
                    fieldVO.setFieldEnName(expr.getName());
                }
            } else if (obj instanceof SQLPropertyExpr) {
                SQLPropertyExpr expr = (SQLPropertyExpr) obj;
                if (aliasField != null) {
                    fieldVO.setFieldEnName(aliasField);
                } else {
                    fieldVO.setFieldEnName(expr.getName());
                }
            } else if (obj instanceof SQLAllColumnExpr) {
                throw new IllegalArgumentException("");
            } else {
                if (aliasField != null) {
                    fieldVO.setFieldEnName(aliasField);
                } else {
                    throw new IllegalArgumentException("");
                }
            }
            // 相同字段名称重复校验
            if (fieldVOList.stream().anyMatch(x -> x.getFieldEnName().equalsIgnoreCase(fieldVO.getFieldEnName()))) {
                throw new IllegalArgumentException("");
            } else {
                fieldVOList.add(fieldVO);
            }
        }
        return fieldVOList;
    }

    /**
     * 解析SQL中字段注释
     * 只是单行注释 -- # 开头
     *
     * @param fieldList
     */
    protected void parseFieldComment(List<SelectSqlParseField> fieldList) {
        for (SelectSqlParseField fieldVO : fieldList) {
            String pattern = "(\\.|\\s|,)+" + fieldVO.getFieldEnName() + "\\s*(#|--)(.*)";
            Pattern p = Pattern.compile(pattern);
            String fieldName = "";
            Matcher m = p.matcher(sql);
            while (m.find()) {
                String comment = m.group(3);
                if (StringUtils.isNotBlank(comment)) {
                    fieldName = comment.trim();
                }
            }
            fieldVO.setFieldName(fieldName);
        }
    }


    public static void main(String[] args) {
        String sql = "SELECT code --测试\n, code \n#我的\n ,name, code \n-- 你的\n FROM db1.test";
        sql = "select SR_BIGINT -- 字段a\n, sum(SR_BIGINT) as _id,test --测试 \nfrom gao_yewu_copy2";
        String pattern = "(\\.|\\s|,)+test\\s*(#|--)(.*)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String comment = m.group(3);
            if (StringUtils.isNotBlank(comment)) {
                System.out.println("comment = " + comment.trim());
            }
            m.appendReplacement(sb, " ");
        }
        m.appendTail(sb);
        System.out.println("StringBuffer = " + sb.toString());
    }
}
