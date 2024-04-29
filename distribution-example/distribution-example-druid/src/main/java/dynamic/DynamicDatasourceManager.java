package dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import druid.TestDruid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.util.*;

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

//    @Autowired
//    IDataSourcePropertyProvider dataSourcePropertyProvider;
//
//    @Autowired
//    DynamicDatasourceExecutor executor;

    @Scheduled(fixedDelay = 60000)
    public void expireHandler() {
        List<String> expireList = new ArrayList<>();
        dataSourceFactory.getCurrentDataSources().forEach((poolName, dataSource) -> {
            if (Objects.equals(poolName, primaryPoolName)) {
                return;
            }
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            Date lastTime = druidDataSource.getDataSourceStat().getConnectionStat().getConnectLastTime();
            log.info("expireHandler {} {}", poolName, DateFormatUtils.format(lastTime, "yyyy-MM-dd HH:mm:ss"));
            if (lastTime == null || System.currentTimeMillis() - lastTime.getTime() < expireAfterAccessMillis) {
                return;
            }
            expireList.add(poolName);
        });
        if (expireList.isEmpty()) {
            return;
        }
        expireList.forEach(poolName -> {
            log.info("expireHandler {} remove", poolName);
//            ReentrantLock lock = executor.getLock(poolName);
//            if (Objects.isNull(lock)) {
//                log.info("expireHandler remove fail because lock is null");
//                return;
//            }
//            try {
//                if (lock.tryLock()) {
//                    dataSourceFactory.removeDataSource(poolName);
//                    executor.removeLock(poolName);
//                }
//            } finally {
//                if (lock.isHeldByCurrentThread()) lock.unlock();
//            }
        });
    }

    @Scheduled(fixedDelay = 10000)
    public void modifyHandler() {
        Map<String, DataSourceProperty> waitModifyMapping = new HashMap<>(10);
        dataSourceFactory.getCurrentDataSources().forEach((poolName, dataSource) -> {
            if (Objects.equals(poolName, primaryPoolName)) {
                return;
            }
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
//            DataSourceProperty dataSourceProperty = dataSourcePropertyProvider.build(poolName);
//            if (Objects.equals(druidDataSource.getUrl(), dataSourceProperty.getUrl())
//                    && Objects.equals(druidDataSource.getUsername(), dataSourceProperty.getUsername())
//                    && Objects.equals(druidDataSource.getPassword(), dataSourceProperty.getPassword())) {
//                return;
//            }
//            waitModifyMapping.put(poolName, dataSourceProperty);
        });
        if (waitModifyMapping.isEmpty()) {
            return;
        }
        waitModifyMapping.forEach((poolName, dataSourceProperty) -> {
            log.info("modifyHandler {} modify", poolName);
            dataSourceFactory.removeDataSource(poolName);
            DataSource dataSource = this.createDataSource(dataSourceProperty);
            dataSourceFactory.addDataSource(poolName, dataSource);
        });
    }

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

}
