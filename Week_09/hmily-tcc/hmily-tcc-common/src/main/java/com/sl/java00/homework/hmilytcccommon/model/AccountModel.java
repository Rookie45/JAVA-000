package com.sl.java00.homework.hmilytcccommon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel implements Serializable {

    private static final long serialVersionUID = 8229285651832609221L;

    private Integer id;
    private Integer userId;
    private BigDecimal balanceRMB;
    private BigDecimal balanceUSD;
    private Timestamp modifyTime;
    private Timestamp createTime;
}
