package org.shaotang.distribution.example.metalmodel.oracle;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.metamodel.DataContext;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.factory.DataContextFactoryRegistry;
import org.apache.metamodel.factory.DataContextFactoryRegistryImpl;
import org.apache.metamodel.factory.DataContextPropertiesImpl;
import org.apache.metamodel.query.*;
import org.apache.metamodel.query.parser.JdbcDataContextFactoryExt;
import org.apache.metamodel.schema.MutableSchema;
import org.apache.metamodel.schema.MutableTable;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
public class OracleMetamodelService {

    public static final String url = "jdbc:oracle:thin:@//10.2.214.9:1521/helowinXDB";
    public static final String tableName = "lddtest1";
    public static final String schema = "test";


    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUsername("test");
        dataSource.setPassword("test");

        dataSource.setInitialSize(10);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(10);
        dataSource.setMaxWait(8000);
        dataSource.setTimeBetweenEvictionRunsMillis(10000);
        dataSource.setMinEvictableIdleTimeMillis(6000000);
        dataSource.setMaxEvictableIdleTimeMillis(9000000);
        dataSource.setTestWhileIdle(true);
        dataSource.setValidationQuery("select 1 from dual");
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setKeepAlive(true);
        //设置错误重试次数,跳出循环设置为true
        dataSource.setConnectionErrorRetryAttempts(1);
        dataSource.setTimeBetweenConnectErrorMillis(2000);
        dataSource.setBreakAfterAcquireFailure(true);
        return dataSource;
    }

    public DataContext getDataContext(DataSource dataSource) {
        DataContextPropertiesImpl properties = new DataContextPropertiesImpl();
        properties.setDataContextType("jdbcExt");
        properties.put(DataContextPropertiesImpl.PROPERTY_DATA_SOURCE, dataSource);
        DataContextFactoryRegistry defaultInstance = DataContextFactoryRegistryImpl.getDefaultInstance();
        defaultInstance.addFactory(new JdbcDataContextFactoryExt());
        DataContext dc = defaultInstance.createDataContext(properties);
        return dc;
    }


    public void testQuery() {

        DataSource dataSource = getDataSource();
        DataContext dc = getDataContext(dataSource);
        String querySql = "select f_code,f_name from " + tableName + " where f_id=1";

        List<Map<String, Object>> datas = new ArrayList<>();
        executeSql(dataSource, querySql, datas::add);
        System.out.println("datas:" + JSONUtil.toJsonStr(datas));
//        Query query = dc.parseQuery(querySql);
        Query query = new Query();

        query.select(new SelectItem("f_code", "f_code"), new SelectItem("f_name", "f_name"));

        query.from(new FromItem(new MutableTable(tableName, new MutableSchema(schema))));
        query.where(new FilterItem(new SelectItem("f_id", null), OperatorType.EQUALS_TO, 1));
        DataSet rows = dc.executeQuery(query);

        List<Map<String, Object>> list = new ArrayList<>();
        if (rows != null) {
            final List<SelectItem> selectItemList = query.getSelectClause().getItems();

            Map<SelectItem, String> selectNameMap = new HashMap<>();

            rows.forEach(row -> {
                Map<String, Object> map = new TreeMap<>();
                int i = 1;
                for (SelectItem selectItem : selectItemList) {
                    Object value = row.getValue(selectItem);
                    String columnName = selectNameMap.get(selectItem);
                    if (columnName == null) {
                        String alias = selectItem.getAlias();
                        if (alias != null && !alias.isEmpty()) {
                            columnName = alias;
                        } else {
                            columnName = selectItem.toString();
                            if (columnName.contains(".")) {
                                String tableAliasName = columnName.substring(0, columnName.indexOf("."));
                                String realColumnName = columnName.substring(columnName.indexOf(".") + 1);

                            }
                        }
                        columnName = columnName.replaceAll("`", "");
                        if (map.containsKey(columnName)) {
                            columnName = columnName + "_" + i++;
                        }
                        selectNameMap.put(selectItem, columnName);
                    }
                    map.put(columnName, value);
                }
                list.add(map);
            });
            rows.close();
            System.out.println("list:" + JSONUtil.toJsonStr(list));
        }
    }

    public Boolean executeSql(DataSource ds, String sql, Consumer<Map<String, Object>> handler) {
        log.info("待执行sql {}", sql);
        Connection connection;
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            log.error("获取连接失败", e);
            return false;
        }
        try {
            try (Statement statement = connection.createStatement()) {
                if (handler == null) {
                    return statement.execute(sql);
                }
                try (ResultSet rs = statement.executeQuery(sql)) {
                    ResultSetMetaData md = rs.getMetaData();
                    int columnCount = md.getColumnCount();
                    while (rs.next()) {
                        Map<String, Object> data = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            data.put(md.getColumnLabel(i), rs.getObject(i));
                        }
                        handler.accept(data);
                    }
                }
            }
        } catch (SQLException e) {
            log.error("执行失败 {}", sql, e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.warn("释放连接失败", e);
            }
        }
        return true;
    }


    public static void main(String[] args) {
        new OracleMetamodelService().testQuery();
    }
}
