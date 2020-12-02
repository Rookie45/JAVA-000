**执行`mysqld --initialize=insecure --user=mysql`抛出丢失MSVCR120.dll错误**

解决办法：

下载[VC redist packages for x64](https://www.microsoft.com/en-us/download/details.aspx?id=40784)，安装其中的`vcredist_x64.exe`，这里我的系统是x64。



**执行`CREATE USER 'repl'@'%' IDENTIFIED BY '123456';`抛出ERROR 1290 (HY000): Unknown error 1290**

解决办法：

先执行` flush privileges;`，在执行上述语句即可



**配置完从节点，然后执行`show slave status\G `，发现下面两个变量值为No**

```mysql
      ...
      Slave_IO_Running: No
     Slave_SQL_Running: No
     ...
```

解决办法：

```mysql
mysql> stop slave;
Query OK, 0 rows affected, 1 warning (0.00 sec)

mysql> SET GLOBAL SQL_SLAVE_SKIP_COUNTER=1; START SLAVE;
Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.07 sec)

mysql> show slave status\G
*************************** 1. row ***************************
               Slave_IO_State: Waiting for master to send event
                  Master_Host: localhost
                  Master_User: repl
                  Master_Port: 3306
                Connect_Retry: 60
              Master_Log_File: mysql-bin.000002
          Read_Master_Log_Pos: 1310
               Relay_Log_File: sl19272-relay-bin.000002
                Relay_Log_Pos: 883
        Relay_Master_Log_File: mysql-bin.000002
             Slave_IO_Running: Yes
            Slave_SQL_Running: Yes
```



存疑：为什么需要SQL_SLAVE_SKIP_COUNTER=1，跳过1位，而不是不用跳，或者多跳几位？
