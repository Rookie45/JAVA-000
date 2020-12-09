package com.sl.java00.homework.hmilytccinvetory.mapper;

import com.sl.java00.homework.hmilytccinvetory.model.InventoryModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMapper {

    int update(InventoryModel inventory);

    InventoryModel selectByProductId(@Param("productId") Integer productId);

}
