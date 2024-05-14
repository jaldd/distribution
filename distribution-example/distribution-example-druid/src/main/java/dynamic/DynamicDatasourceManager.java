package dynamic;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import druid.TestDruid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * @DATE 2022/2/21
 * @AUTHOR congfeng
 */
@Slf4j
@Configuration
@EnableScheduling
public class DynamicDatasourceManager {

    @Value("${datasource.dynamic.expire-after-access-millis:3000000}")
    Long expireAfterAccessMillis;

    @Value("${spring.datasource.dynamic.primary}")
    String primaryPoolName;

    @Value("${spring.datasource.dynamic.druid.break-after-acquire-failure}")
    Boolean breakAfterAcquireFailure;

    @Value("${spring.datasource.dynamic.druid.time-between-connect-error-millis}")
    Long timeBetweenConnectErrorMillis;

    @Autowired
    DataSourceCreator dataSourceCreator;

    @Autowired
    DynamicRoutingDataSource dataSourceFactory;

    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        try {

            Properties pro = new Properties();

            pro.load(TestDruid.class.getClassLoader().getResourceAsStream("druid.properties"));
            return DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            log.error("创建数据源失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> T execute(DataSourceProperty dataSourceProperty, Supplier<T> supplier) {

        if (!dataSourceFactory.getCurrentDataSources().containsKey(dataSourceProperty.getPoolName())) {
            DataSource dataSource = createDataSource(dataSourceProperty);
            dataSourceFactory.addDataSource(dataSourceProperty.getPoolName(), dataSource);
        }
        log.info("create datasource success ,unlock");
        try {
            DynamicDataSourceContextHolder.push(dataSourceProperty.getPoolName());
            return supplier.get();
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }
}
