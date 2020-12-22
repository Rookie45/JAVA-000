package com.sl.java00.homework.hmilytccconsumer.service.impl;

import com.sl.java00.homework.hmilytcccommon.api.TransactionService;
import com.sl.java00.homework.hmilytcccommon.dto.TransactionDTO;
import com.sl.java00.homework.hmilytcccommon.mapper.AccountMapper;
import com.sl.java00.homework.hmilytcccommon.mapper.FreezeMapper;
import com.sl.java00.homework.hmilytcccommon.model.AccountModel;
import com.sl.java00.homework.hmilytcccommon.model.FreezeModel;
import com.sl.java00.homework.hmilytccconsumer.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.sl.java00.homework.hmilytcccommon.model.BalanceType.RMB;
import static com.sl.java00.homework.hmilytcccommon.model.BalanceType.USD;

/**
 * A 库为consumer，B 库为provider
 */
@Slf4j
@Service
public class ConsumerServiceImpl implements ConsumerService {

    //汇率
    private final static BigDecimal Rate = new BigDecimal(7.0);

    private FreezeMapper freezeMapper;

    private AccountMapper accountMapper;

//    @DubboReference
    private TransactionService transactionService;

    @Autowired(required = false)
    public ConsumerServiceImpl(FreezeMapper freezeMapper, AccountMapper accountMapper, TransactionService transactionService) {
        this.freezeMapper = freezeMapper;
        this.accountMapper = accountMapper;
        this.transactionService = transactionService;
    }

    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean transactionUSD2RMB(TransactionDTO dto) {

        // 1. 查询A中的账户余额
        AccountModel accountA = findByUserId(dto.getConsumerUserId());
        BigDecimal balanceUSD = accountA.getBalanceUSD();

        BigDecimal transactionUSD = dto.getAccount();
        if (transactionUSD.compareTo(balanceUSD) > 0) {
            log.error("[ConsumerServiceImpl][transactionUSD2RMB] account USD insufficient balance ");
            throw new RuntimeException("insufficient balance");
        }
        accountA.setBalanceUSD(balanceUSD.subtract(transactionUSD));

        // 2. 冻结A中的1美元
        FreezeModel freezeModel = new FreezeModel();
        freezeModel.setAccountId(accountA.getId());
        freezeModel.setFreezeAmount(transactionUSD);
        freezeModel.setFreezeType(USD.getCode());
        // 暂不处理返回false的情况
        freezeAccount(freezeModel, accountA);

        // 3. 冻结B中的7人民币
        transactionService.freezeAccount(dto);

        log.info("[ConsumerServiceImpl][transactionUSD2RMB] =========进行try操作完成================ ");
        return true;
    }

    // 4. confirm时，对A增加7人民币，对B进行增加1美元，并清除冻结表
    @Transactional
    public void confirm(TransactionDTO dto) {
        log.info("[TransactionServiceImpl][freezeAccount] begin confirm {}", dto);
        AccountModel accountA = findByUserId(dto.getConsumerUserId());
        BigDecimal balanceRMB = accountA.getBalanceRMB();
        accountA.setBalanceRMB(balanceRMB.add(dto.getAccount().multiply(Rate)));
        accountMapper.update(accountA);
        transactionService.updateAccount(dto);
        freezeMapper.deleteByAccountId(accountA.getId());
        log.info("[ConsumerServiceImpl][confirm] =========进行confirm操作完成================ ");
    }

    // 5. cancel时，对A增加1美元，对B进行增加7人民币，并清除冻结表
    @Transactional
    public void cancel(TransactionDTO dto) {
        AccountModel accountA = findByUserId(dto.getConsumerUserId());
        BigDecimal balanceUSD = accountA.getBalanceUSD();
        accountA.setBalanceUSD(balanceUSD.add(dto.getAccount()));
        accountMapper.update(accountA);
        dto.setType(RMB.getCode());
        dto.setAccount(dto.getAccount().multiply(Rate));
        transactionService.updateAccount(dto);
        freezeMapper.deleteByAccountId(accountA.getId());
        log.info("[ConsumerServiceImpl][cancel] =========进行cancel操作完成================ ");
    }

    @Transactional
    boolean freezeAccount(FreezeModel freeze, AccountModel account) {
        log.info("[ConsumerServiceImpl][freezeAccount] freeze A account {}, {} ", freeze, account);
        return 0 < freezeMapper.save(freeze)
                && 0 < accountMapper.update(account);
    }

    private AccountModel findByUserId(Integer userId) {
        return accountMapper.selectByUserId(userId);
    }
}
