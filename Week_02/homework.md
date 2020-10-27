**题目1：**

使用 GCLogAnalysis.java 自己演练一遍串行/并行/CMS/G1的案例。   

**答题如下**：

> **串行GC，堆大小512M**

```java
Java HotSpot(TM) 64-Bit Server VM (25.191-b12) for windows-amd64 JRE (1.8.0_191-b12), built on Oct  6 2018 09:29:03 by "java_re" with MS VC++ 10.0 (VS2010)
Memory: 4k page, physical 16611316k(9464840k free), swap 17659892k(9832372k free)
CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseSerialGC  
2020-10-25T16:28:30.492+0800: 0.224: [GC (Allocation Failure) 2020-10-25T16:28:30.492+0800: 0.224: [DefNew: 139776K->17472K(157248K), 0.0391739 secs] 139776K->46387K(506816K), 0.0393111 secs] [Times: user=0.03 sys=0.00, real=0.05 secs] 
...
2020-10-25T16:28:31.367+0800: 1.092: [Full GC (Allocation Failure) 2020-10-25T16:28:31.367+0800: 1.092: [Tenured: 349499K->349393K(349568K), 0.0399489 secs] 506730K->372468K(506816K), [Metaspace: 3794K->3794K(1056768K)], 0.0399981 secs] [Times: user=0.03 sys=0.00, real=0.03 secs] 
Heap
 def new generation   total 157248K, used 72766K [0x00000000e0000000, 0x00000000eaaa0000, 0x00000000eaaa0000)
  eden space 139776K,  52% used [0x00000000e0000000, 0x00000000e470fb50, 0x00000000e8880000)
  from space 17472K,   0% used [0x00000000e9990000, 0x00000000e9990000, 0x00000000eaaa0000)
  to   space 17472K,   0% used [0x00000000e8880000, 0x00000000e8880000, 0x00000000e9990000)
 tenured generation   total 349568K, used 349393K [0x00000000eaaa0000, 0x0000000100000000, 0x0000000100000000)
   the space 349568K,  99% used [0x00000000eaaa0000, 0x00000000fffd4600, 0x00000000fffd4600, 0x0000000100000000)
 Metaspace       used 3801K, capacity 4540K, committed 4864K, reserved 1056768K
  class space    used 419K, capacity 428K, committed 512K, reserved 1048576K
```

截取了使用串行垃圾收集器的一次GC和一次Full GC，简单分析：

GC发生的原因是分配内存失败`Allocation Failure`，年轻代使用串行收集器`DefNew`回收（Serial收集器中的新生代名为Default New Generation），使用空间从139776KB减少到17472KB，总的年轻代可用内存为157248KB，GC耗时约39ms；堆（heap）的内存使用从139776KB减少到46387KB，总的可用内存为506816KB，GC耗时39ms；可以根据上面的堆和新生代的信息，可以得出此处GC，有46387-17472=28915KB的对象晋升老年代，且老年代总的可用空间为506816KB-157248KB=349568KB。后面的时间统计，`user`部分表示所有 GC线程消耗的CPU时间，此处为30ms，`sys`部分表示系统调用和系统等待事件消耗的时间（线程上下文切换），此处为0ms，`real`表示应用程序暂停的时间，此处为30ms。

  Full GC发生的原因也是分配内存失败`Allocation Failure`，GC发生的区域为老年代（Tenured），也是使用的串行GC，回收后老年代空间从349499KB减少到349393KB，总的老年代可使用空间349568KB，GC耗时约40ms；堆（heap）的内存使用从506730KB减少到372468KB，总的可用内存为506816KB；metaspace没有发生变化。总的时间统计，GC线程消耗30ms，上线文切换0ms，总的GC执行时间为30ms。



> **并行GC，堆大小512M**



> **CMS GC，堆大小512M**



> **G1 GC，堆大小512M**



> **串行 GC，堆大小1024M**



> **并行GC，堆大小1024M**



> **CMS GC，堆大小1024M**



> **G1 GC，堆大小1024M**



> **串行 GC，堆大小2048M**



> **并行GC，堆大小2048M**



> **CMS GC，堆大小2048M**



> **G1 GC，堆大小2048M**



> **串行 GC，堆大小4086M**



> **并行GC，堆大小4086M**



> **CMS GC，堆大小4086M**



> **G1 GC，堆大小4086M**





**题目2：**

使用压测工具（wrk或sb），演练gateway-server-0.0.1-SNAPSHOT.jar 示例  

**答题如下**：

```java
PS E:\Book\训练营\week2\day03> sb -u http://localhost:8088/api/hello -c 20 -N 60
Starting at 2020/10/26 23:21:33
[Press C to stop the test]
422769  (RPS: 6627.5)
---------------Finished!----------------
Finished at 2020/10/26 23:22:37 (took 00:01:03.9202427)
Status 200:    422771

RPS: 6914.2 (requests/second)
Max: 79ms
Min: 0ms
Avg: 0.1ms

  50%   below 0ms
  60%   below 0ms
  70%   below 0ms
  80%   below 0ms
  90%   below 0ms
  95%   below 0ms
  98%   below 2ms
  99%   below 3ms
99.9%   below 12ms
```

> 程序启动参数为：` java -jar -Xmx1g -Xms1g .\gateway-server-0.0.1-SNAPSHOT.jar`。依据上面的数据可得，吞吐量为6914每秒，最大延迟为79ms，最低为0ms，平均0.1ms。再次压测，分别用**jvisualvm**和**jmc**工具对压测过程中内存使用和GC情况进行监控统计。

![jvisualvm](E:\Code\java_geekbang\JAVA-000\Week_02\gateway-jvisualvm.png)

> 上述图为**jvisualvm**工具监控内存做的数据统计，每次波峰到波谷的跳变，即发生一次GC，图中总共发生27次GC。

![gateway-jmc1](E:\Code\java_geekbang\JAVA-000\Week_02\gateway-jmc1.PNG)

![gateway-jmc2](E:\Code\java_geekbang\JAVA-000\Week_02\gateway-jmc2.PNG)

![gateway-jmc3](E:\Code\java_geekbang\JAVA-000\Week_02\gateway-jmc3.PNG)

> 上述三张图为**jmc**工具监控的数据统计，利用了它的飞行器功能，统计1分钟内程序运行中发生的情况。可以简要看出整个压测过程中，堆平均使用187MB，最大使用356MB，CPU平均使用63.8%，最大使用99.6%，GC暂停时间平均2ms608us，最大3ms990us；程序运行的PC运行的线程数为4*8=32，物理内存为16GB，默认情况下，GC线程为CPU的1/4，即为8；默认heap的大小为总内存的1/4，即4GB，而由于程序启动时指定了堆大小1GB，所以统计显示的是1GB，java8默认使用的垃圾收集器为并行收集器，与统计信息相符合。

```java
PS E:\Book\训练营\week2\day03> jmap -heap 16980
Attaching to process ID 16980, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.191-b12

using thread-local object allocation.
Parallel GC with 8 thread(s)
...
```



**题目3：**

如果自己本地有可以运行的项目，可以按照2的方式进行演练  

**答题如下**：

```java
PS E:\Book\训练营\week2\day03> sb -u http://localhost:28089/admin/test -c 20 -N 60
Starting at 2020/10/27 7:56:28
[Press C to stop the test]
163604  (RPS: 2539.8)
---------------Finished!----------------
Finished at 2020/10/27 7:57:32 (took 00:01:04.4926086)
Status 200:    163605

RPS: 2676.9 (requests/second)
Max: 136ms
Min: 0ms
Avg: 3.6ms

  50%   below 2ms
  60%   below 3ms
  70%   below 4ms
  80%   below 5ms
  90%   below 7ms
  95%   below 10ms
  98%   below 14ms
  99%   below 18ms
99.9%   below 42ms
```

程序启动参数为：` java -jar -Xms1g -Xmx1g .\newbee-mall-1.0.0-SNAPSHOT.jar`。依据上面的数据可得，吞吐量为2676每秒，最大延迟为136ms，最低为0ms，平均3.6ms。接着分别用**jvisualvm**和**jmc**工具对压测过程中内存使用和GC情况进行监控统计。

![mall-jvisualvm](E:\Code\java_geekbang\JAVA-000\Week_02\mall-jvisualvm.PNG)

> 上述图为**jvisualvm**工具监控内存做的数据统计，压测时间[8:01:36, 8:02:40]每次波峰到波谷的跳变，即发生一次GC，图中总共发生21次GC。

![mall-jmc1](E:\Code\java_geekbang\JAVA-000\Week_02\mall-jmc1.PNG)

![mall-jmc2](E:\Code\java_geekbang\JAVA-000\Week_02\mall-jmc2.PNG)

> 上述两张图为**jmc**工具监控的数据统计，统计1分钟内程序运行中发生的情况。可以简要看出整个压测过程中，堆平均使用410MB，最大使用614MB，CPU平均使用81.2%，最大使用100%，GC暂停时间平均18ms537us，最大35ms332us；初始堆大小和最大堆大小为1GB，使用的垃圾收集器为并行收集器，共发生了72次GC，这里能看到最短暂停7ms811us。



**题目4：**

根据上述自己对于1和2的演示，写一段对于不同 GC 的总结，提交到 Github。  

**答题如下**：



**题目5：**

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





**题目6：**

写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801，代码提交到
Github  

**答题如下：**
```java
package java00.week02;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public class MyHttpClient {
    private static final String URL = "http://localhost:8801";
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
//            .setSocketTimeout(1000)
            .build();
    private static final CloseableHttpClient HTTP_CLIENT = HttpClientBuilder.create()
            .build();//默认重试3次

    public static void main(String[] args) {
        System.out.println(httpGet(URL));
    }

    public static String httpGet(String url) {
        String response = null;
        if (null == url) {
            return response;
        }
        HttpGet httpGet = null;
        try {
            URI uri = new URIBuilder(url).build();
            httpGet = new HttpGet(uri);
            response = execute(httpGet);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String execute(HttpRequestBase requestBase) {
        String responseResult = null;
        CloseableHttpResponse response = null;
        try {
            requestBase.setConfig(REQUEST_CONFIG);
            response = HTTP_CLIENT.execute(requestBase);
            if (null == response) {
                return responseResult;
            }
            HttpEntity responseEntity = response.getEntity();
            if (null == responseEntity) {
                return responseResult;
            }
            responseResult = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return responseResult;
    }
}

package java00.week02;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MyOkHttp {
    private static final String URL = "http://localhost:8801";
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(5L, TimeUnit.SECONDS)
            .readTimeout(5L, TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) {
        System.out.println(httpGet(URL));
    }

    public static String httpGet(String url) {
        String response = null;
        if (null == url) {
            return response;
        }
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        response = execute(request);
        return response;
    }

    private static String execute(Request request) {
        String responseResult = null;
        try (Response response = HTTP_CLIENT.newCall(request).execute()){
            if (null == response) {
                return responseResult;
            }
            if (response.isSuccessful() && null != response.body()) {
                responseResult =response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseResult;
    }
}
```


参考

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

[如何选择合适的 GC 时间 —— USER, SYS, REAL？](https://cloud.tencent.com/developer/article/1491229)

[HttpClient、OkHttp、RestTemplate、WebClient的基本使用](https://www.jianshu.com/p/afc96b7de90c)

