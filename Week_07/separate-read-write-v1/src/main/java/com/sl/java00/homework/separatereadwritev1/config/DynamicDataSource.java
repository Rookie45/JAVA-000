package com.sl.java00.homework.separatereadwritev1.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceTypeEnum source = DynamicDataSourceContextHolder.getCurrentDataSource();
        log.info("[DynamicDataSource][determineCurrentLookupKey] data source is {}", source);
        return null == source ? DataSourceTypeEnum.MASTER : source;
    }
}
