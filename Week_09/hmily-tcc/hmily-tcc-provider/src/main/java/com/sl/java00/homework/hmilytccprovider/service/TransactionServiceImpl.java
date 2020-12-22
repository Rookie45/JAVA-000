package com.sl.java00.homework.hmilytccprovider.service;

import com.sl.java00.homework.hmilytcccommon.api.TransactionService;
import com.sl.java00.homework.hmilytcccommon.dto.TransactionDTO;
import com.sl.java00.homework.hmilytcccommon.mapper.AccountMapper;
import com.sl.java00.homework.hmilytcccommon.mapper.FreezeMapper;
import com.sl.java00.homework.hmilytcccommon.model.AccountModel;
import com.sl.java00.homework.hmilytcccommon.model.FreezeModel;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.sl.java00.homework.hmilytcccommon.model.BalanceType.RMB;

@Slf4j
@Service(value = "transactionService")
//@DubboService(retries = 0)
public class TransactionServiceImpl implements TransactionService {

    //汇率
    private final static BigDecimal Rate = new BigDecimal(7.0);

    private AccountMapper accountMapper;

    private FreezeMapper freezeMapper;

    @Autowired(required = false)
    public TransactionServiceImpl(AccountMapper accountMapper, FreezeMapper freezeMapper) {
        this.accountMapper = accountMapper;
        this.freezeMapper = freezeMapper;
    }

    // 冻结B中的7人民币，暂不处理返回false的情况
    @Override
    @HmilyTCC(confirmMethod = "confirmFreeze", cancelMethod = "cancelFreeze")
    public void freezeAccount(TransactionDTO dto) {
        log.info("[TransactionServiceImpl][freezeAccount] begin freeze Account B {}", dto);
        AccountModel accountB = findByUserId(dto.getProviderUserId());
        BigDecimal balanceRMB = accountB.getBalanceRMB();

        BigDecimal transactionRMB = dto.getAccount().multiply(Rate);
        if (transactionRMB.compareTo(balanceRMB) > 0) {
            log.error("[TransactionServiceImpl][freezeAccount] account RMB insufficient balance {}", balanceRMB);
            throw new RuntimeException("insufficient balance");
        }
        accountB.setBalanceRMB(balanceRMB.subtract(transactionRMB));

        FreezeModel freezeModel = new FreezeModel();
        freezeModel.setAccountId(accountB.getId());
        freezeModel.setFreezeAmount(transactionRMB);
        freezeModel.setFreezeType(RMB.getCode());
        freezeAccount(freezeModel, accountB);
        log.info("[ConsumerServiceImpl][freezeAccount] =========进行B freezeAccount try操作完成================ ");
    }


    // 配合consumer的confirm和 cancel，进行B 库中确认或者回退
    @Override
//    @HmilyTCC(confirmMethod = "confirmAccount", cancelMethod = "cancelAccount")
    public void updateAccount(TransactionDTO dto) {
        log.info("[TransactionServiceImpl][updateAccount] update B account: {} ", dto);
        AccountModel accountB = findByUserId(dto.getProviderUserId());

        // 如果type是人民币，则增加B的人民币余额
        // 如果type是美元，则增加B的美元余额
        if (RMB.getCode() == dto.getType()) {
            BigDecimal balanceRMB = accountB.getBalanceRMB();
            accountB.setBalanceRMB(balanceRMB.add(dto.getAccount()));
        } else {
            BigDecimal balanceUSD = accountB.getBalanceUSD();
            accountB.setBalanceUSD(balanceUSD.add(dto.getAccount()));
        }
        accountMapper.update(accountB);
        freezeMapper.deleteByAccountId(accountB.getId());
    }

    public void confirmFreeze() {
        log.info("=========进行confirmFreeze操作完成================");
    }

    public void cancelFreeze() {
        log.info("=========进行cancelFreeze操作完成================");
    }

    public void confirmAccount() {
        log.info("=========进行confirmAccount操作完成================");
    }

    public void cancelAccount() {
        log.info("=========进行cancelAccount操作完成================");
    }

    @Transactional
    boolean freezeAccount(FreezeModel freeze, AccountModel account) {
        log.info("[TransactionServiceImpl][freezeAccount] freeze B account {}, {} ", freeze, account);
        return 0 < freezeMapper.save(freeze)
                && 0 < accountMapper.update(account);
    }

    private AccountModel findByUserId(Integer userId) {
        return accountMapper.selectByUserId(userId);
    }
}
