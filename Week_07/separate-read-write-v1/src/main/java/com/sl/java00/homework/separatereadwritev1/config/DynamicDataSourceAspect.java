package com.sl.java00.homework.separatereadwritev1.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class DynamicDataSourceAspect {

    @Value("${slave.hosts}")
    private String slaveHosts;

    private AtomicInteger index = new AtomicInteger(0);

    @Pointcut("@annotation(com.sl.java00.homework.separatereadwritev1.config.DS)")
    public void dataSourcePointcut() {
    }

    @Around("dataSourcePointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        DataSourceTypeEnum ds = getDsAnnotation(joinPoint);
        DynamicDataSourceContextHolder.setCurrentDataSource(ds);
        try {
            return joinPoint.proceed();
        } finally {
            DynamicDataSourceContextHolder.removeDataSource();
        }
    }

    private DataSourceTypeEnum getDsAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DS ds = signature.getMethod().getAnnotation(DS.class);
        if (Objects.nonNull(ds)) {
            if (!DataSourceTypeEnum.MASTER.equals(ds.value())) {
                return slaveLoadBalance();
            }
        }
        return DataSourceTypeEnum.MASTER;
    }

    private DataSourceTypeEnum slaveLoadBalance() {
        String[] slaves = slaveHosts.split(",");
        int index = Math.abs(this.index.getAndAdd(1) % slaves.length);
        return DataSourceTypeEnum.SLAVE1.getName().equals(slaves[index]) ?
                DataSourceTypeEnum.SLAVE1 : DataSourceTypeEnum.SLAVE2;
    }
}
