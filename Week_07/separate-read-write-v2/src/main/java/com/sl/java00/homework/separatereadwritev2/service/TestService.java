package com.sl.java00.homework.separatereadwritev2.service;

import com.sl.java00.homework.separatereadwritev2.mapper.TestMapper;
import com.sl.java00.homework.separatereadwritev2.model.TestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TestService {
    @Autowired
    private TestMapper testMapper;

    public TestModel getTest(Integer id) {
        return testMapper.selectById(id);
    }

    public List<TestModel> getAll() {
        return testMapper.selectAll();
    }

    @Transactional
    public int save(TestModel testModel) {
        List<TestModel> before = testMapper.selectAll();
        log.info("[TestService][save] before save, db has {}", before);

        int result = testMapper.save(testModel);

        List<TestModel> after = testMapper.selectAll();
        log.info("[TestService][save] after save, db has {}", after);
        return result;
    }
}
