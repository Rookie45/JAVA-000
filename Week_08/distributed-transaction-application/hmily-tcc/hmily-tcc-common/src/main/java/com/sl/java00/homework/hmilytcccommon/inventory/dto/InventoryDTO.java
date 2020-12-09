package com.sl.java00.homework.hmilytcccommon.inventory.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class InventoryDTO implements Serializable {

    private static final long serialVersionUID = 6731796047646742891L;

    private Integer productId;

    private Integer lockStock;

    private Integer stock;
}
