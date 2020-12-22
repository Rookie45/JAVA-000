package io.kimmking.rpcfx.demo.provider.config;

import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.demo.provider.service.DemoResolver;
import io.kimmking.rpcfx.demo.provider.service.OrderServiceImpl;
import io.kimmking.rpcfx.demo.provider.service.UserServiceImpl;
import io.kimmking.rpcfx.server.RpcfxInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public RpcfxInvoker createInvoker(@Autowired RpcfxResolver resolver){
        return new RpcfxInvoker(resolver);
    }

    @Bean
    public RpcfxResolver createResolver(){
        return new DemoResolver();
    }

    // 能否去掉name
    // 将字符串传递改为类型传递，解析即可
    @Bean//(name = "io.kimmking.rpcfx.demo.api.UserService")
    public UserService createUserService(){
        return new UserServiceImpl();
    }

    @Bean//(name = "io.kimmking.rpcfx.demo.api.OrderService")
    public OrderService createOrderService(){
        return new OrderServiceImpl();
    }
}
