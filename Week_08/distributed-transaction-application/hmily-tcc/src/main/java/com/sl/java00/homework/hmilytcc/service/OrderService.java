package com.sl.java00.homework.hmilytcc.service;

import com.sl.java00.homework.hmilytcc.mapper.OrderMapper;
import com.sl.java00.homework.hmilytcc.model.OrderModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public int save(OrderModel orderModel) {
        return orderMapper.save(orderModel);
    }

    public int update(OrderModel orderModel) {
        return orderMapper.update(orderModel);
    }

    public int delete(Integer id) {
        return orderMapper.delete(id);
    }
}
