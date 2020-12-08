package com.sl.java00.homework.hmilytcc.controller;

import com.sl.java00.homework.hmilytcc.model.OrderModel;
import com.sl.java00.homework.hmilytcc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/test")
    public ResponseEntity<?> findOne(@RequestParam(name = "id") Integer id) {
        OrderModel test = orderService.getTest(id);
        return new ResponseEntity<>(test, HttpStatus.OK);
    }

    @GetMapping("/tests")
    public ResponseEntity<?> findAll() {
        List<OrderModel> tests = orderService.getAll();
        return new ResponseEntity<>(tests, HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<?> save(@RequestBody OrderModel test) {
        log.info("[TestController][save] test model is {}", test);
        int result = orderService.save(test);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    @PutMapping("/test")
    public ResponseEntity<?> update(@RequestBody OrderModel test) {
        log.info("[TestController][save] test model is {}", test);
        int result = orderService.update(test);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/test")
    public ResponseEntity<?> deleteOne(@RequestParam(name = "id") Integer id) {
        int result = orderService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
    }
}
