package com.sl.java00.springboot.homework.lesson10.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NameTypeHandler extends BaseTypeHandler {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, (String) o);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return parseName(resultSet.getString(s));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return parseName(resultSet.getString(i));
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return parseName(callableStatement.getString(i));
    }

    //自定义类型处理器
    private String parseName(String value) {
        return "spring" + value;
    }
}
