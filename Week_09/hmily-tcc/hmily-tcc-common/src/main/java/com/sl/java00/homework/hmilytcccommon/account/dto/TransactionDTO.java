package com.sl.java00.homework.hmilytcccommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO implements Serializable, Cloneable {

    private static final long serialVersionUID = 5182719963109090492L;

    private Integer consumerUserId;
    private Integer providerUserId;
    private BigDecimal account;
    private Integer type;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
