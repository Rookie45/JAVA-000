package com.sl.java00.homework.hmilytccinvetory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryModel implements Serializable {

    private static final long serialVersionUID = 729285057757747265L;

    private Integer id;
    private Integer productId;
    private String skuCode;
    private Integer stock;
    private Integer lowStock;
    private Integer lockStock;
    private Timestamp modifyTime;
    private Timestamp createTime;
}
