//package io.github.kimmking.gateway.filter;
//
//import io.github.kimmking.gateway.router.RandomHttpEndpointRouter;
//import io.github.kimmking.gateway.router.RibbonHttpEndpointRouter;
//import io.github.kimmking.gateway.router.WeightHttpEndpointRouter;
//
//import java.util.Collection;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * 创建filter对象缓存
// */
//public class FilterRegistry {
//
//    private Map<String, Object> filterMap = new ConcurrentHashMap<>();
//
//    public FilterRegistry() {
//        put(PreHttpRequestFilter.class.getName(), new PreHttpRequestFilter());
////        put(RandomHttpEndpointRouter.class.getName(), new RandomHttpEndpointRouter());
//        put(RibbonHttpEndpointRouter.class.getName(), new RibbonHttpEndpointRouter());
////        put(WeightHttpEndpointRouter.class.getName(), new WeightHttpEndpointRouter());
//    }
//
//    public void put(String name, Object filter) {
//        this.filterMap.put(name, filter);
//    }
//
//    public int size() {
//        return this.filterMap.values().size();
//    }
//
//    public Collection<Object> getAllFilters() {
//        return this.filterMap.values();
//    }
//}
