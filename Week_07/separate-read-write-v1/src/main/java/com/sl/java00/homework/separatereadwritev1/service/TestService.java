package com.sl.java00.homework.separatereadwritev1.service;

import com.sl.java00.homework.separatereadwritev1.config.DS;
import com.sl.java00.homework.separatereadwritev1.config.DataSourceTypeEnum;
import com.sl.java00.homework.separatereadwritev1.mapper.TestMapper;
import com.sl.java00.homework.separatereadwritev1.model.TestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestMapper testMapper;

    // @DS(value = DataSourceTypeEnum.SLAVE2)
    public TestModel getTest(Integer id) {
        return testMapper.selectById(id);
    }

    // @DS(value = DataSourceTypeEnum.SLAVE1)
    public List<TestModel> getAll() {
        return testMapper.selectAll();
    }

    @DS(value = DataSourceTypeEnum.MASTER)
    public int save(TestModel testModel) {
        return testMapper.save(testModel);
    }
}
