**题目1：**

运行课上的例子，以及 Netty 的例子，分析相关现象。  

**答题如下**：

```java
PS E:\Book\训练营\week2\day03> sb -u http://localhost:8801 -c 20 -N 60
Starting at 2020/10/27 8:30:09
[Press C to stop the test]
2031    (RPS: 31.6)
---------------Finished!----------------
Finished at 2020/10/27 8:31:14 (took 00:01:04.3449906)
2045    (RPS: 31.8)                     Status 200:    2045

RPS: 33.4 (requests/second)
Max: 645ms
Min: 51ms
Avg: 587.7ms

  50%   below 595ms
  60%   below 608ms
  70%   below 617ms
  80%   below 623ms
  90%   below 623ms
  95%   below 624ms
  98%   below 625ms
  99%   below 625ms
99.9%   below 626ms

PS E:\Book\训练营\week2\day03> sb -u http://localhost:8802 -c 20 -N 60
Starting at 2020/10/27 8:33:31
[Press C to stop the test]
38530   (RPS: 600.2)
---------------Finished!----------------
Finished at 2020/10/27 8:34:35 (took 00:01:04.2749597)
Status 200:    38520
Status 303:    12

RPS: 630.6 (requests/second)
Max: 83ms
Min: 14ms
Avg: 27ms

  50%   below 27ms
  60%   below 28ms
  70%   below 29ms
  80%   below 30ms
  90%   below 31ms
  95%   below 33ms
  98%   below 36ms
  99%   below 39ms
99.9%   below 58ms
    
PS E:\Book\训练营\week2\day03> sb -u http://localhost:8803 -c 20 -N 60
Starting at 2020/10/27 8:35:57
[Press C to stop the test]
39108   (RPS: 608.4)
---------------Finished!----------------
Finished at 2020/10/27 8:37:01 (took 00:01:04.3220522)
Status 200:    39101
Status 303:    7

RPS: 640.6 (requests/second)
Max: 113ms
Min: 13ms
Avg: 26.4ms

  50%   below 27ms
  60%   below 28ms
  70%   below 29ms
  80%   below 29ms
  90%   below 30ms
  95%   below 31ms
  98%   below 34ms
  99%   below 38ms
99.9%   below 72ms

PS E:\Book\训练营\week2\day03> sb -u http://localhost:8808/test -c 20 -N 60
Starting at 2020/10/27 8:40:28
[Press C to stop the test]
372229  (RPS: 5797.8)
---------------Finished!----------------
Finished at 2020/10/27 8:41:32 (took 00:01:04.4027045)
Status 200:    372238

RPS: 6081.2 (requests/second)
Max: 98ms
Min: 0ms
Avg: 0ms

  50%   below 0ms
  60%   below 0ms
  70%   below 0ms
  80%   below 0ms
  90%   below 0ms
  95%   below 0ms
  98%   below 0ms
  99%   below 2ms
99.9%   below 3ms
```

`HttpServer01`在每次请求来时，都是main线程处理，也就导致所有请求串行处理，所以四种实现中，压测吞吐量最低。`HttpServer02`在每次请求来时，都new一个thread处理，压测吞吐量630.6 每秒，但这非常消耗线程资源，且线程的创建和销毁是非常消耗时间。`HttpServer03`在每次请求来时，都交给创建的线程池处理，压测吞吐量640.6每秒，这里线程资源可控。到了`NettyServerApplication`，吞吐量直接涨了一个数量级，压测是6081.2每秒，而且平均延迟也是最低0ms，Netty将原先的同步改为了异步，基于事件驱动，最大程度上接受请求。

**题目2：**

写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801，代码提交到
Github  

**答题如下：**

HttpClient的实现：
[CustomHttpClient.java](https://github.com/Rookie45/JAVA-000/blob/main/Week_02/CustomHttpClient.java)

OkHttp的实现：
[CustomOkHttp.java](https://github.com/Rookie45/JAVA-000/blob/main/Week_02/CustomOkHttp.java)



**参考**

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

[HttpClient、OkHttp、RestTemplate、WebClient的基本使用](https://www.jianshu.com/p/afc96b7de90c)