package io.kimmking.rpcfx.demo.api;

import io.kimmking.rpcfx.common.BB;

public interface UserService {

    @BB
    User findById(int id);

}
