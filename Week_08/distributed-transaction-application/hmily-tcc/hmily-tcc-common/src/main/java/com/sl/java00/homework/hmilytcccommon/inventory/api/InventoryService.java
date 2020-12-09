package com.sl.java00.homework.hmilytcccommon.inventory.api;

import com.sl.java00.homework.hmilytcccommon.inventory.dto.InventoryDTO;
import org.dromara.hmily.annotation.Hmily;

public interface InventoryService {

    /**
     * 扣减库存操作
     * @param inventoryDTO 库存DTO对象
     * @return true boolean
     */
    @Hmily
    Boolean decrease(InventoryDTO inventoryDTO);

    InventoryDTO findByProductId(Integer productId);
}
