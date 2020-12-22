package com.sl.java00.homework.hmilytcccommon.mapper;

import com.sl.java00.homework.hmilytcccommon.model.AccountModel;
import org.springframework.stereotype.Repository;

public interface AccountMapper {
    int update(AccountModel accountModel);

    AccountModel selectByUserId(Integer id);
}
