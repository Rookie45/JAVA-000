package com.sl.java00.homework.separatedb.service;

import com.sl.java00.homework.separatedb.mapper.OrderMapper;
import com.sl.java00.homework.separatedb.model.OrderModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public OrderModel getTest(Integer id) {
        return orderMapper.selectById(id);
    }

    public List<OrderModel> getAll() {
        return orderMapper.selectAll();
    }

    @Transactional
    public int save(OrderModel orderModel) {
        List<OrderModel> before = orderMapper.selectAll();
        log.info("[TestService][save] before save, db has {}", before);

        int result = orderMapper.save(orderModel);

        List<OrderModel> after = orderMapper.selectAll();
        log.info("[TestService][save] after save, db has {}", after);
        return result;
    }
}
