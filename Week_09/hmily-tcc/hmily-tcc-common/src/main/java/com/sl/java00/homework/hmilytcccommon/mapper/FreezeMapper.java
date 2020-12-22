package com.sl.java00.homework.hmilytcccommon.mapper;

import com.sl.java00.homework.hmilytcccommon.model.FreezeModel;

public interface FreezeMapper {

    int save(FreezeModel model);

    int update(FreezeModel model);

    int deleteByAccountId(Integer accountId);
}
