package com.sl.java00.homework.separatereadwritev1.mapper;

import com.sl.java00.homework.separatereadwritev1.config.DS;
import com.sl.java00.homework.separatereadwritev1.config.DataSourceTypeEnum;
import com.sl.java00.homework.separatereadwritev1.model.TestModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestMapper {

    int save(TestModel t1);

    @DS(value = DataSourceTypeEnum.SLAVE)
    TestModel selectById(@Param("id") Integer id);

    @DS(value = DataSourceTypeEnum.SLAVE)
    List<TestModel> selectAll();
}
