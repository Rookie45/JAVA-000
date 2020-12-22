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
public class FreezeModel implements Serializable {

    private static final long serialVersionUID = 568685429905399214L;

    private Integer id;
    private Integer accountId;
    private BigDecimal freezeAmount;
    private Integer freezeType;
    private Timestamp modifyTime;
    private Timestamp createTime;
}
