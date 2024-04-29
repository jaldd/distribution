package druid.parse;

import druid.parse.select.SqlParseResult;

/**
 * SQL解析
 *
 * @author zhaob-e
 * @date 2021/7/29
 */
public interface SqlParser {

    /**
     * 解析创建SQL语句
     *
     * @return
     */
    SqlParseResult parseCreateSql();
}
