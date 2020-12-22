package com.sl.java00.homework.hmilytcccommon.api;

import com.sl.java00.homework.hmilytcccommon.dto.TransactionDTO;
import org.dromara.hmily.annotation.Hmily;

public interface TransactionService {

    @Hmily
    void freezeAccount(TransactionDTO dto);

    @Hmily
    void updateAccount(TransactionDTO dto);

//    TransactionDTO findByUserId(Integer consumerUserId);
}
