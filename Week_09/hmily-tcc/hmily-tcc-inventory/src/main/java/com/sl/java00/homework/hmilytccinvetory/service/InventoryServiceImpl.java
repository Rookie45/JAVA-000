package com.sl.java00.homework.hmilytccinvetory.service;

import com.sl.java00.homework.hmilytcccommon.inventory.api.InventoryService;
import com.sl.java00.homework.hmilytcccommon.inventory.dto.InventoryDTO;
import com.sl.java00.homework.hmilytccinvetory.mapper.InventoryMapper;
import com.sl.java00.homework.hmilytccinvetory.model.InventoryModel;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "inventoryService")
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryMapper inventoryMapper;

    @Autowired
    public InventoryServiceImpl(InventoryMapper inventoryMapper) {
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    @HmilyTCC(confirmMethod = "confirmInventory", cancelMethod = "cancelInventory")
    public Boolean decrease(InventoryDTO inventory) {
        log.info("[InventoryServiceImpl][decrease] decrease inventory:{}", inventory);
        InventoryModel model = getByProductId(inventory.getProductId());
        model.setStock(model.getStock() - inventory.getLockStock());
        model.setLockStock(model.getLockStock() + inventory.getLockStock());
        return inventoryMapper.update(model) > 0;
    }

    @Override
    public InventoryDTO findByProductId(Integer productId) {
        log.info("[InventoryServiceImpl][findByProductId] find inventory by productId:{}", productId);
        return model2DTO(getByProductId(productId));
    }

    private InventoryModel getByProductId(Integer productId) {
        return inventoryMapper.selectByProductId(productId);
    }

    public void confirmInventory(InventoryDTO inventory) {
        log.info("=========进行库存confirm操作完成================");
    }

    public void cancelInventory(InventoryDTO inventory) {
        log.info("=========进行库存confirm操作完成================");
    }

    private InventoryDTO model2DTO(InventoryModel model) {
        InventoryDTO dto = new InventoryDTO();
        dto.setProductId(model.getProductId());
        dto.setStock(model.getStock());
        dto.setLockStock(model.getLockStock());
        return dto;
    }
}
