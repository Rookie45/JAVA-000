win7下搭建kafk-manager



1. 前期环境准备

   JDK要求1.8

   安装[scala-2.12.12](https://downloads.lightbend.com/scala/2.12.12/scala-2.12.12.msi)

   安装[sbt-1.4.5](https://github.com/sbt/sbt/releases/download/v1.4.5/sbt-1.4.5.msi)

   下载[kafka-manager-2.0.0.2](https://github.com/yahoo/CMAK/archive/2.0.0.2.zip)

   > kafka-manager 3.x的版本需要JDK 11以上

2. 修改kafka-manager的配置文件

   ```java
   #kafka-manager.zkhosts="kafka-manager-zookeeper:2181"
   kafka-manager.zkhosts="localhost:2181"
   ```

3. 找到解压kafka-manager所在目录，shift+右击，选择“在此处打开命令窗口”，执行命令

   ```
   sbt clean dist
   ```

4. 解压生成的kafka-manager的zip包，运行kafka-manager

5. 浏览器输入http://localhost:9000/访问kafka-manager的主页
