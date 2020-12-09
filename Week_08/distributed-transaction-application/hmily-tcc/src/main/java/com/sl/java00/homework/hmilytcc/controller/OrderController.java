package com.sl.java00.homework.hmilytccorder.controller;

import com.sl.java00.homework.hmilytccorder.model.OrderModel;
import com.sl.java00.homework.hmilytccorder.service.impl.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * {
     * "id":10,
     * "orderId":10,
     * "orderSn":"bbbbb",
     * "orderStatus":0,
     * "productId":28,
     * "productName":"goods1",
     * "productSn":"zxcvasd",
     * "productQuantity":1,
     * "skuId":20,
     * "skuCode":"20201208121212"
     * }
     * @param order order
     * @return response
     */
    @PostMapping("/test")
    public ResponseEntity<?> transactOrder(@RequestBody OrderModel order) {
        log.info("[TestController][save] order request is {}", order);
        Boolean result = orderService.orderTransaction(order);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/tests")
    public ResponseEntity<?> findAll() {
        List<OrderModel> tests = orderService.getAll();
        return new ResponseEntity<>(tests, HttpStatus.OK);
    }


}
