package com.sl.java00.homework.hmilytccorder.service;

import com.sl.java00.homework.hmilytccorder.model.OrderModel;

public interface TransactionService {
    /**
     * 订单支付
     *
     * @param order 订单实体
     */
    Boolean transaction(OrderModel order);
}
