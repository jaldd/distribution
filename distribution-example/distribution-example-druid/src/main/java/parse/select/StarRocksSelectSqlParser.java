package parse.select;

import com.alibaba.druid.DbType;

/**
 * Starrocks查询SQL解析器
 *
 * @author xinzp
 * @date 2022/09/04
 */
public class StarRocksSelectSqlParser extends MysqlSelectSqlParser {

    public StarRocksSelectSqlParser(String sql, Integer dataSourceType) {
        super(sql, dataSourceType);
    }

    @Override
    protected DbType getDbType() {
        return DbType.starrocks;
    }
}
