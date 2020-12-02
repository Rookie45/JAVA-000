package com.sl.java00.homework.separatereadwritev1.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfig {

    @Bean(name = "master")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slave1")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave1")
    public DataSource slave1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slave2")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave2")
    public DataSource slave2DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DataSource dynamicDataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>(3);
        dataSourceMap.put(DataSourceTypeEnum.MASTER, masterDataSource());
        dataSourceMap.put(DataSourceTypeEnum.SLAVE1, slave1DataSource());
        dataSourceMap.put(DataSourceTypeEnum.SLAVE2, slave2DataSource());

        //设置动态数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        //设置所有数据源
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        //当拿不到数据源时，采用此数据源，也就是master数据源
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource());

        return dynamicDataSource;
    }
}
