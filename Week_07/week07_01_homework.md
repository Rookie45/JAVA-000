**题目**

1、用今天课上学习的知识，分析自己系统的SQL和表结构

**答题如下：**

**题目**
2、按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。

**答题如下：**

| 插入方式                                                  | 100万订单插入时间 |
| --------------------------------------------------------- | ----------------- |
| 单条                                                      | 8个小时以上       |
| PreparedStatement+单条                                    | 8个小时以上       |
| Add Batch                                                 | 554.229s          |
| Add Batch+PreparedStatement                               | 473.903s          |
| 开启rewriteBatchedStatements，Add Batch+PreparedStatement | 275.659s          |
| load data                                                 | 66.2s             |

**题目**

3、按自己设计的表结构，插入1000万订单模拟数据，测试不同方式的插入效率。

**答题如下：**

**题目**

4、使用不同的索引或组合，测试不同方式查询效率。

**答题如下：**

**题目**

5、调整测试数据，使得数据尽量均匀，模拟1年时间内的交易，计算一年的销售报表：销售总额，订单数，客单价，每月销售量，前十的商品等等（可以自己设计更多指标）。

**答题如下：**

**题目**

6、尝试自己做一个ID生成器（可以模拟Seq或Snowflake）。

**答题如下：**

[SnowflakeIdGenerator.java](https://github.com/Rookie45/JAVA-000/blob/main/Week_07/SnowflakeIdGenerator.java)

**题目**

7、尝试实现或改造一个非精确分页的程序。

**答题如下：**


**参考**

[HikariCP MySQL Configuration](https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration)

[分布式唯一ID生成器](https://www.liaoxuefeng.com/article/1280526512029729#0)
