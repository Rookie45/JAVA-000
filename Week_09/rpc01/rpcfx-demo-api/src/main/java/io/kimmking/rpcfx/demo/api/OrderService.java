package io.kimmking.rpcfx.demo.api;

import io.kimmking.rpcfx.common.BB;

public interface OrderService {

    @BB
    Order findOrderById(int id);

}
