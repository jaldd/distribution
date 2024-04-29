package druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Date;
import java.util.Properties;

public class TestDruid {

    public static void main(String[] args) throws Exception {

        Properties pro = new Properties();

        pro.load(TestDruid.class.getClassLoader().getResourceAsStream("druid.properties"));
        DataSource ds = DruidDataSourceFactory.createDataSource(pro);
        DruidDataSource druidDataSource = (DruidDataSource) ds;
        System.out.println("-------");

        Connection conn = ds.getConnection();
        Date lastTime = druidDataSource.getDataSourceStat().getConnectionStat().getConnectLastTime();
        System.out.println(lastTime);

    }
}