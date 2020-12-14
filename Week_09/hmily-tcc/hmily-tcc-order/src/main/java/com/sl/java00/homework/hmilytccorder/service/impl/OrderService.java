package com.sl.java00.homework.hmilytccorder.service.impl;

import com.sl.java00.homework.hmilytccorder.mapper.OrderMapper;
import com.sl.java00.homework.hmilytccorder.model.OrderModel;
import com.sl.java00.homework.hmilytccorder.model.OrderStatusEnum;
import com.sl.java00.homework.hmilytccorder.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {

    private final OrderMapper orderMapper;

    private final TransactionService transactionService;

    @Autowired(required = false)
    public OrderService(OrderMapper orderMapper, TransactionService transactionService) {
        this.orderMapper = orderMapper;
        this.transactionService = transactionService;
    }

    public List<OrderModel> getAll() {
        return orderMapper.selectAll();
    }

    /**
     * 这里思考一下，为什么通过状态来标记成功失败，一方面业务确实有多个状态
     * 另一方面，失败了只是更新字段，比起删除操作更具安全性
     *
     * @param order order
     * @return transaction success or failed, true to success, otherwise failed.
     */
    public Boolean orderTransaction(OrderModel order) {
        // 库存数据库里仅一条记录，商品id为28, skuCode为20201208121212
        order.setProductId(28);
        order.setSkuCode("20201208121212");
        return save(order) && transactionService.transaction(order);
    }

    private Boolean save(OrderModel order) {
        log.info("[OrderService][save] begin to save order.");
        order.setOrderStatus(OrderStatusEnum.NOT_PAY.getCode());
        return orderMapper.save(order) > 0;
    }

}
