package com.sl.java00.homework.hmilytccorder.mapper;

import com.sl.java00.homework.hmilytccorder.model.OrderModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {

    int save(OrderModel o1);

    int update(OrderModel o1);

    OrderModel selectById(@Param("id") Integer id);

    List<OrderModel> selectAll();
}
