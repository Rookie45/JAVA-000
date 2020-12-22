package com.sl.java00.homework.hmilytccconsumer.controller;

import com.sl.java00.homework.hmilytcccommon.dto.TransactionDTO;
import com.sl.java00.homework.hmilytccconsumer.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ConsumerController {

    private ConsumerService consumerService;

    @Autowired
    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @PostMapping("/test")
    public ResponseEntity<?> transactBalance(@RequestBody TransactionDTO dto) {
        log.info("[ConsumerController][transactBalance] order request is {}", dto);
        boolean result = consumerService.transactionUSD2RMB(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
