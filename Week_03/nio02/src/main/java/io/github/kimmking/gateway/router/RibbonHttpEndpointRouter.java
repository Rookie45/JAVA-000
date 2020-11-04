package io.github.kimmking.gateway.router;

import java.util.List;

/**
 * 单机情况下的轮询
 */
public class RibbonHttpEndpointRouter implements HttpEndpointRouter {
    //这里假设至少有一个endpoint
    private static Integer pos = 0;

    @Override
    public String route(List<String> endpoints) {
        int size = endpoints.size();
        String reslut = null;
        synchronized (pos) {
            if (pos >= size) {
                pos = 0;
            }
            reslut = endpoints.get(pos);
            pos++;
        }
        return reslut;
    }
}
