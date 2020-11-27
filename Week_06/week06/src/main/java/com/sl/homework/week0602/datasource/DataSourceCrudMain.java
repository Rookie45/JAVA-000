package com.sl.homework.week0602.datasource;


import static com.sl.homework.week0602.datasource.HikariDataSourceCrud.hikariMillionInsert;
import static com.sl.homework.week0602.datasource.HikariDataSourceCrud.hikariMillionUpdate;

public class DataSourceCrudMain {

    static String insertSql = "INSERT INTO tb_order " +
            "(order_id,order_sn,user_id,business_id,product_snapshot,pay_amount,order_status,note,delete_status,payment_time,modify_time,create_time)" +
            "VALUES( ?, ?, ?, ?, ?, 130.00, 1, ?, 0, now(), now(), now()) ;";

    static String updateSql = "update tb_order set pay_amount=2 where delete_status=?;";

    public static void main(String[] args) {
//        hikariMillionInsert(insertSql);
        hikariMillionUpdate(updateSql);
    }








}
