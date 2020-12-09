项目结构：
hmily-tcc-demo
  - hmily-tcc-common 用于暴露invetory的接口
  - hmily-tcc-invetory 库存模块
  - hmily-tcc-order 订单模块
  
外层hmily-tcc-demo的pom.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.sl.java00.homework</groupId>
    <artifactId>hmily-tcc-demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.1.RELEASE</version>
    </parent>

    <modules>
        <module>hmily-tcc-common</module>
        <module>hmily-tcc-order</module>
        <module>hmily-tcc-invetory</module>
    </modules>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <jdk.version>1.8</jdk.version>
        <hmily.version>2.1.2-SNAPSHOT</hmily.version>
        <mysql.version>5.1.47</mysql.version>
        <dubbo.version>2.6.5</dubbo.version>
        <zookeeper.version>3.6.0</zookeeper.version>
        <curator.version>5.1.0</curator.version>
        <mybatis.version>2.1.4</mybatis.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.dromara</groupId>
                <artifactId>hmily-annotation</artifactId>
                <version>${hmily.version}</version>
            </dependency>

            <dependency>
                <groupId>org.dromara</groupId>
                <artifactId>hmily-core</artifactId>
                <version>${hmily.version}</version>
            </dependency>

            <dependency>
                <groupId>org.dromara</groupId>
                <artifactId>hmily-dubbo</artifactId>
                <version>${hmily.version}</version>
            </dependency>

            <dependency>
                <groupId>org.dromara</groupId>
                <artifactId>hmily-spring-boot-starter-dubbo</artifactId>
                <version>${hmily.version}</version>
            </dependency>

        </dependencies>
    </dependencyManagement>

    <dependencies>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis.version}</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>${dubbo.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>${zookeeper.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

    </dependencies>

</project>
```

```
package com.sl.java00.homework.hmilytcccommon.inventory.api;

import com.sl.java00.homework.hmilytcccommon.inventory.dto.InventoryDTO;
import org.dromara.hmily.annotation.Hmily;

public interface InventoryService {

    /**
     * 扣减库存操作
     * @param inventoryDTO 库存DTO对象
     * @return true boolean
     */
    @Hmily
    Boolean decrease(InventoryDTO inventoryDTO);

    InventoryDTO findByProductId(Integer productId);
}
package com.sl.java00.homework.hmilytcccommon.inventory.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class InventoryDTO implements Serializable {

    private static final long serialVersionUID = 6731796047646742891L;

    private Integer productId;

    private Integer lockStock;

    private Integer stock;
}

```


hmily-tcc-common的pom.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.sl.java00.homework</groupId>
        <artifactId>hmily-tcc-demo</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

    <artifactId>hmily-tcc-common</artifactId>
    <name>hmily-tcc-common</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>

        <dependency>
            <groupId>org.dromara</groupId>
            <artifactId>hmily-annotation</artifactId>
        </dependency>

        <!--spring boot的核心启动器-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

    </dependencies>


</project>

```

hmily-tcc-invetory的pom.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.sl.java00.homework</groupId>
        <artifactId>hmily-tcc-demo</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

    <artifactId>hmily-tcc-invetory</artifactId>

    <name>hmily-tcc-invetory</name>
    <packaging>jar</packaging>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.dromara</groupId>
            <artifactId>hmily-dubbo</artifactId>
            <version>${hmily.version}</version>
        </dependency>

        <dependency>
            <groupId>com.sl.java00.homework</groupId>
            <artifactId>hmily-tcc-common</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>

    </dependencies>

    <build>
        <plugins>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                <configuration>
                    <source>${jdk.version}</source>
                    <target>${jdk.version}</target>
                    <encoding>${project.build.sourceEncoding}</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.sl.java00.homework.hmilytccinvetory.HmilyTccInventoryApplication</mainClass>
                    <executable>true</executable>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```
hmily-tcc-order的pom.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.sl.java00.homework</groupId>
        <artifactId>hmily-tcc-demo</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

    <artifactId>hmily-tcc-order</artifactId>
    <packaging>jar</packaging>
    <name>hmily-tcc-order</name>

    <description>Demo project for Spring Boot</description>

    <dependencies>

        <dependency>
            <groupId>org.dromara</groupId>
            <artifactId>hmily-spring-boot-starter-dubbo</artifactId>
            <version>${hmily.version}</version>
        </dependency>

        <dependency>
            <groupId>com.sl.java00.homework</groupId>
            <artifactId>hmily-tcc-common</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>

    </dependencies>

    <build>
        <plugins>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                <configuration>
                    <source>${jdk.version}</source>
                    <target>${jdk.version}</target>
                    <encoding>${project.build.sourceEncoding}</encoding>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.sl.java00.homework.hmilytccorder.HmilyTccOrderApplication</mainClass>
                    <executable>true</executable>
                </configuration>
            </plugin>

        </plugins>
    </build>
</project>

```
