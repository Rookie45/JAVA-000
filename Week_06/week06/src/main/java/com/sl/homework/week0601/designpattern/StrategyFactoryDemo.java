package com.sl.homework.week0601.designpattern;

import java.util.HashMap;

public class StrategyFactoryDemo {

    private static HashMap<String, Strategy> strategyMap = new HashMap<>();

    //提前加载好，这个可以结合spring，在spring启动时放入spring容器，由于spring管理生命周期
    static {
        strategyMap.put("A", new AStrategy());
        strategyMap.put("B", new BStrategy());
    }

    //通过反射new对象
    static <T extends Strategy> T getStrategy(Class<T> sClass) throws IllegalAccessException, InstantiationException {
        return sClass.newInstance();
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
//        strategyMap.get("A").doAction();

        getStrategy(AStrategy.class).doAction();
    }

    interface Strategy {
        void doAction();
    }

    static class AStrategy implements Strategy {
        @Override
        public void doAction() {
            System.out.println("this is Strategy A!!!");
        }
    }

    static class BStrategy implements Strategy {
        @Override
        public void doAction() {
            System.out.println("this is Strategy B!!!");
        }
    }
}
