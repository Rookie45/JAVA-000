package com.sl.java00.homework.separatedb.mapper;

import com.sl.java00.homework.separatedb.model.OrderModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {

    int save(OrderModel t1);

    OrderModel selectById(@Param("id") Integer id);

    List<OrderModel> selectAll();
}
