wind7搭建ActiveMQ步骤

1. 下载ActiveMQ软件包

   [apache-activemq-5.16.0](http://www.apache.org/dyn/closer.cgi?filename=/activemq/5.16.0/apache-activemq-5.16.0-bin.zip&action=download)

2. 环境准备

   - JDK需要1.7及以上版本
   - 设置JAVA_HOME的环境变量，即将其值设置为安装JDK时的目录（已存在就忽略这步）
   - Maven需要3.0及以上版本（当编译源码或者开发者需要的环境，如果不是编译源码，用的二进制，忽略这步）

3. 启动ActiveMQ

   找到解压ActiveMQ所在目录，进入bin目录，shift+右击，选择“在此处打开命令窗口”，执行`activemq start`命令，activemq起来之后

   ```java
   D:\program\apache\apache-activemq-5.16.0\bin>activemq start
   ...
    INFO | Apache ActiveMQ 5.16.0 (localhost, ID:sl19272-50120-1610162289656-0:1) i
   s starting
    INFO | Listening for connections at: tcp://sl19272:61616?maximumConnections=100
   0&wireFormat.maxFrameSize=104857600
    INFO | Connector openwire started
    INFO | Listening for connections at: amqp://sl19272:5672?maximumConnections=100
   0&wireFormat.maxFrameSize=104857600
    INFO | Connector amqp started
    INFO | Listening for connections at: stomp://sl19272:61613?maximumConnections=1
   000&wireFormat.maxFrameSize=104857600
    INFO | Connector stomp started
    INFO | Listening for connections at: mqtt://sl19272:1883?maximumConnections=100
   0&wireFormat.maxFrameSize=104857600
    INFO | Connector mqtt started
    INFO | Starting Jetty server
    INFO | Creating Jetty connector
    WARN | ServletContext@o.e.j.s.ServletContextHandler@33308786{/,null,STARTING} h
   as uncovered http methods for path: /
    INFO | Listening for connections at ws://sl19272:61614?maximumConnections=1000&
   wireFormat.maxFrameSize=104857600
    INFO | Connector ws started
    INFO | Apache ActiveMQ 5.16.0 (localhost, ID:sl19272-50120-1610162289656-0:1) s
   tarted
    INFO | For help or more information please see: http://activemq.apache.org
    INFO | ActiveMQ WebConsole available at http://127.0.0.1:8161/
    INFO | ActiveMQ Jolokia REST API available at http://127.0.0.1:8161/api/jolokia
   /
   ```

4. 检测ActiveMQ是否启动成功

   由于ActiveMQ默认启动后使用61616端口，所以可以使用netstat命令查询一下此端口是否被使用，如下所示该端口处于监听状态，即ActiveMQ启动成功

   ```
   D:\>netstat -an | find "61616"
     TCP    0.0.0.0:61616          0.0.0.0:0              LISTENING
     TCP    [::]:61616             [::]:0                 LISTENING
   ```

5. ActiveMQ监控平台

   使用浏览器访问http://localhost:8161/admin，默认用户名/密码为admin/admin

6. 暂停ActiveMQ

   找到刚才启动ActiveMQ的目录，即bin目录，打开命令提示窗口，执行`activemq stop`

7. 自定义配置启动ActiveMQ

   可以通过修改conf/activemq.xml配置文件，让activeMQ以自己配置启动

   [activemq.xml](http://activemq.apache.org/xml-configuration)
