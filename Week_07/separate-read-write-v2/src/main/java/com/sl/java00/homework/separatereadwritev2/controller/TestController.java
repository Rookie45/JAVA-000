package com.sl.java00.homework.separatereadwritev2.controller;

import com.sl.java00.homework.separatereadwritev2.model.TestModel;
import com.sl.java00.homework.separatereadwritev2.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResponseEntity<?> findOne(@RequestParam(name = "id") Integer id) {
        TestModel test = testService.getTest(id);
        return new ResponseEntity<>(test, HttpStatus.OK);
    }

    @GetMapping("/tests")
    public ResponseEntity<?> findAll() {
        List<TestModel> tests = testService.getAll();
        return new ResponseEntity<>(tests, HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<?> save(@RequestBody TestModel test) {
        log.info("[TestController][save] test model is {}", test);
        int result = testService.save(test);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
