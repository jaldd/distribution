package test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import druid.TestDruid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class TestJdbcTemplate {


    public static Map<String, DruidDataSource> map = new HashMap<>();

    public void testConnect() throws Exception {
        Thread thread = new Thread(new MyRunnable());
        // 启动线程
        thread.start();
        Thread.sleep(10000);
        log.info("expireHandler {}", map.get("datasource").getDataSourceStat().getConnectionStat().getConnectLastTime());
    }

    public static void main(String[] args) throws Exception {

        new TestJdbcTemplate().testConnect();


    }


    class MyRunnable implements Runnable {

        @Override
        public void run() {
            System.out.println("新线程开始运行，参数为: ");
            Properties pro = new Properties();
            try {
                pro.load(TestDruid.class.getClassLoader().getResourceAsStream("druid.properties"));
                DataSource ds = null;
                ds = DruidDataSourceFactory.createDataSource(pro);
                DruidDataSource druidDataSource = (DruidDataSource) ds;
//                druidDataSource.getConnection();
                System.out.println("-------");
                map.put("datasource", druidDataSource);
//        log.info("expireHandler {}", druidDataSource.getDataSourceStat().getConnectionStat().getConnectLastTime());
                JdbcTemplate jdbcTemplate = new JdbcTemplate(druidDataSource);
                jdbcTemplate.update("UPDATE gmdm_integrate.t_catalog\n" +
                        "SET f_tenant_id=10000, f_parent_id=0, f_name='非主数据域', f_logo='1'" +
                        "WHERE f_id=382827;");
                System.out.println("success");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
