package com.sl.java00.homework.hmilytccorder.service.impl;

import com.sl.java00.homework.hmilytcccommon.inventory.api.InventoryService;
import com.sl.java00.homework.hmilytcccommon.inventory.dto.InventoryDTO;
import com.sl.java00.homework.hmilytccorder.mapper.OrderMapper;
import com.sl.java00.homework.hmilytccorder.model.OrderModel;
import com.sl.java00.homework.hmilytccorder.model.OrderStatusEnum;
import com.sl.java00.homework.hmilytccorder.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final OrderMapper orderMapper;

    private final InventoryService inventoryService;

    @Autowired(required = false)
    public TransactionServiceImpl(OrderMapper orderMapper, InventoryService inventoryService) {
        this.orderMapper = orderMapper;
        this.inventoryService = inventoryService;
    }

    /**
     * 在进行交易前，先检查资源是否符合要求，在进行资源锁定，成功或失败后更新状态和对应的
     *
     * @param order 订单实体
     * @return transaction success or failed, true to success, otherwise failed.
     */
    @HmilyTCC(confirmMethod = "confirmOrder", cancelMethod = "cancelOrder")
    public Boolean transaction(OrderModel order) {
        log.info("[TransactionServiceImpl][transaction] begin to transact order.");
        InventoryDTO inventoryDTO = inventoryService.findByProductId(order.getProductId());
        if (inventoryDTO.getStock() < order.getProductQuantity()) {
            throw new RuntimeException("库存不足");
        }

        return updateOrderStatus(order, OrderStatusEnum.PAYING)
                && inventoryService.decrease(buildInventoryDTO(order));
    }

    private Boolean updateOrderStatus(OrderModel order, OrderStatusEnum orderStatus) {
        order.setOrderStatus(orderStatus.getCode());
        return orderMapper.update(order) > 0;
    }

    private InventoryDTO buildInventoryDTO(OrderModel order) {
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setProductId(order.getProductId());
        inventoryDTO.setLockStock(order.getProductQuantity());
        return inventoryDTO;
    }

    public void confirmOrder(OrderModel order) {
        updateOrderStatus(order, OrderStatusEnum.PAY_SUCCESS);
        log.info("=========进行订单confirm操作完成================");
    }

    public void cancelOrder(OrderModel order) {
        updateOrderStatus(order, OrderStatusEnum.PAY_FAIL);
        log.info("=========进行订单cancel操作完成================");
    }
}
