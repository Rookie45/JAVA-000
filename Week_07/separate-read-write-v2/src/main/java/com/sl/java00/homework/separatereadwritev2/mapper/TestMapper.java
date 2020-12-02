package com.sl.java00.homework.separatereadwritev2.mapper;

import com.sl.java00.homework.separatereadwritev2.model.TestModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestMapper {

    int save(TestModel t1);

    TestModel selectById(@Param("id") Integer id);

    List<TestModel> selectAll();
}
