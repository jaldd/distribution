package org.shaotang.distribution.example.starrocks.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired(required = false)
    JdbcTemplate jdbcTemplate;

    //调用http://127.0.0.1:8001/register 测试
    @RequestMapping("test")
    public Integer test() {

        int update = jdbcTemplate.update("INSERT INTO test.detailDemo (recruit_date, region_num, num_plate, tel, id, password, name, profile, hobby, leave_time, " +
                "channel, income, account, ispass) VALUES ('2022-03-14', 2, 1212, 1231231231, 123412341234, '123452342342343324', 'hello', " +
                "'welcome', 'starrocks', '2022-03-15 12:21:32', 123.04, 21.12345, 123456.1235, 0);");
        return update;
    }
}