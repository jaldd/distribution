//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.shaotang.sequence.dao;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Date;
@Slf4j
public class SequenceService {


    public boolean insert(Connection conn, String sequenceName, long currentValue, Date date, long maxValue, long minValue) {
        String sql = "insert into MY_SEQUENCE(SEQUENCE_NAME,CURRENT_VALUE,MAX_VALUE,MIN_VALUE,CREATE_TIME,LAST_UPDATE_TIME) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, sequenceName);
            ps.setLong(2, currentValue);
            ps.setLong(3, maxValue);
            ps.setLong(4, minValue);
            Timestamp ts = new Timestamp(date.getTime());
            ps.setTimestamp(5, ts);
            ps.setTimestamp(6, ts);
            int rowCount = ps.executeUpdate();
            return rowCount >= 1;
        } catch (SQLException var22) {
            throw new RuntimeException(var22);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
            } catch (SQLException var23) {
                throw new RuntimeException(var23);
            }

        }
    }

    public boolean update(Connection conn, String sequenceName, long currentValue, long newValue, Date date) {
        String sql = "update MY_SEQUENCE set CURRENT_VALUE=?, LAST_UPDATE_TIME=? where SEQUENCE_NAME=? and CURRENT_VALUE=?";
        PreparedStatement ps = null;

        boolean var11 = false;
        try {
            ps = conn.prepareStatement(sql);
            ps.setLong(1, newValue);
            ps.setTimestamp(2, new Timestamp(date.getTime()));
            ps.setString(3, sequenceName);
            ps.setLong(4, currentValue);
            int rowCount = ps.executeUpdate();
            return rowCount >= 1;
        } catch (SQLException var19) {
            throw new RuntimeException(var19);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
            } catch (SQLException var20) {
                throw new RuntimeException(var20);
            }

        }
    }

    public Long queryForLong(Connection conn, String sequenceName) {
        String sql = "select CURRENT_VALUE from MY_SEQUENCE where SEQUENCE_NAME=? for update";
        PreparedStatement ps = null;
        ResultSet res = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, sequenceName);
            res = ps.executeQuery();
            Long currentValue = null;
            if (res.first()) {
                currentValue = res.getLong("CURRENT_VALUE");
            }

            return  currentValue;
        } catch (SQLException var16) {
            throw new RuntimeException(var16);
        } finally {
            try {
                if (res != null) {
                    res.close();
                    res = null;
                }

                if (ps != null) {
                    ps.close();
                    ps = null;
                }
            } catch (SQLException var15) {
                throw new RuntimeException(var15);
            }

        }
    }
}
