package com.github.giveme0101.config.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name SqlTemplate
 * @Date 2020/10/14 11:17
 */
public abstract class SqlTemplate<T> {

    public abstract T map(ResultSet rs) throws SQLException;

    public T selectOne(Connection conn, String sql, Object[] args){
        List<T> list = select(conn, sql, args);
        return list.get(0);
    }

    public List<T> select(Connection conn, String sql, Object[] args){

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            if (null != args && args.length > 0){
                for (int i = 0; i < args.length; i++) {
                    pstmt.setObject(i + 1, args[i]);
                }
            }

            rs = pstmt.executeQuery();

            List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }

            return list;
        } catch (Exception ex){
            throw new RuntimeException(ex);
        } finally {
            try {
                if (null != rs && !rs.isClosed()) rs.close();
                if (null != pstmt && !pstmt.isClosed()) pstmt.close();
                if (null != conn && !conn.isClosed()) conn.close();
            } catch (Exception ex) {}
        }
    }

}