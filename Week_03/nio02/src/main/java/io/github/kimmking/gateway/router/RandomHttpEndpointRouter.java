package io.github.kimmking.gateway.router;

import java.util.List;
import java.util.Random;

/**
 * 随机路由
 */
public class RandomHttpEndpointRouter implements HttpEndpointRouter {

    @Override
    public String route(List<String> endpoints) {
        int size = endpoints.size();
        int routerId = new Random().nextInt(size);
        return endpoints.get(routerId);
    }
}
