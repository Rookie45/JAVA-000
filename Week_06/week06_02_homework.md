**题目**

1、基于课程中的设计原则和最佳实践，分析是否可以将自己负责的业务系统进行数据库设计或是数据库服务器方面的优化。

**答题如下：**

> 待补充

**题目**

2、基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交DDL的SQL文件到Github（后面2周的作业依然要是用到这个表结构）。

**答题如下：**

[schcema.sql](https://github.com/Rookie45/JAVA-000/blob/main/Week_06/schema.sql)

**题目**

3、尽可能多的从“常见关系数据库”中列的清单，安装运行，并使用上一题的SQL测试简单的增删改查。

**答题如下：**

测试的关系数据库为mysql5.7和postgresql12.5

插入见存储过程批量插入：

[million_data_mysql.sql](https://github.com/Rookie45/JAVA-000/blob/main/Week_06/million_data_mysql.sql)

[million_data_pg.sql](https://github.com/Rookie45/JAVA-000/blob/main/Week_06/million_data_pg.sql)

update tb_order set pay_amount=2 where delete_status=0;

select order_sn from tb_order where order_id > 800000;

delete from tb_order where delete_status=0;

**题目**

4、基于上一题，尝试对各个数据库测试100万订单数据的增删改查性能。

**答题如下：**

|操作 |mysql5.7|postgresql12.5|access2013|
|  ----  | ----  | ----  | ----  |
|  增加  | 00:01:53.22 | 00:00:42.483 | -- |
|  删除  | 00:00:42.84 | 00:00:09.35 | -- |
|  修改  | 00:00:41.19 | 00:00:34.92 | -- |
|  查询  | 00:00:01.28 | 00:00:01.22 | -- |


5、尝试对MySQL不同引擎下测试100万订单数据的增删改查性能。

**答题如下：**

|操作 |InnoDB|MyISAM|MEMORY|ARCHIVE|
|  ----  | ----  | ----  | ----  | ----  |
|  增加  | 00:01:53.22 | 00:01:10.8 | 00:00:30.248 | 00:00:33.727 |
|  删除  | 00:00:42.84 | 00:00:36.723 | 00:00:01.373 | --- |
|  修改  | 00:00:41.19 | 00:00:50.404 | 00:00:01.342 | --- |
|  查询  | 00:00:01.28 | 00:00:00.094 | 00:00:00.405 | 00:00:01.373 |
|  大小  | 132.7M | 125.7M | 1.2G | 10.5M |

> tip:
>
> Memory引擎需要将mysql使用的内存调整，默认是16M，不能存下百万订单的数量
>
> SET max_heap_table_size = 1024 * 1024 * 2048;
>
> SET tmp_table_size = 1024 * 1024 * 2000;

**题目**

6、模拟1000万订单数据，测试不同方式下导入导出（数据备份还原）MySQL的速度，包括jdbc程序处理和命令行处理。思考和实践，如何提升处理效率。

**答题如下：**

> 待补充

**题目**

7、对MySQL配置不同的数据库连接池（DBCP、C3P0、Druid、Hikari），测试增删改查100万次，对比性能，生成报告  

**答题如下：**

> 待补充

**参考**

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

[mall project](http://www.macrozheng.com/#/database/mall_database_overview)

[MySQL存储过程批量插入百万条数据](https://cloud.tencent.com/developer/news/605176)
