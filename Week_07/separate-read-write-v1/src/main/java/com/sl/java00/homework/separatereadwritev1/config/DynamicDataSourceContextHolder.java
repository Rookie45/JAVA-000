package com.sl.java00.homework.separatereadwritev1.config;

public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<DataSourceTypeEnum> DS_CONTEXT_HOLDER = new ThreadLocal<>();

    public static DataSourceTypeEnum getCurrentDataSource() {
        return DS_CONTEXT_HOLDER.get();
    }

    public static void setCurrentDataSource(DataSourceTypeEnum ds) {
        DS_CONTEXT_HOLDER.set(ds);
    }

    public static void removeDataSource() {
        DS_CONTEXT_HOLDER.remove();
    }
}
