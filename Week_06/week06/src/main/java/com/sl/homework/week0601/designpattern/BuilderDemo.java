package com.sl.homework.week0601.designpattern;

/**
 * 建造者模式，常用设计模式，使用多个简单对象构造模板类的实例
 * 推荐用链式编程形式
 */
public class BuilderDemo {
    public static void main(String[] args) {
        VegBurger vegBurger = new VegBurger();
        vegBurger.orderBurger();
        Coke coke = new Coke();
        coke.orderDrink();
        Meal meal = MealBuilder.build()
                .setBurger(vegBurger)
                .setDrink(coke)
                .create();
        System.out.println(meal.toString());

    }

    static class MealBuilder {
        private Meal meal;
        private Burger burger;
        private ColdDrink drink;

        public static MealBuilder build() {
            return new MealBuilder();
        }

        public Meal create() {
            this.meal = new Meal();
            if (null != this.burger)
                meal.setBurger(this.burger);
            if (null != this.drink)
                meal.setDrink(this.drink);
            return meal;
        }

        public MealBuilder setBurger(Burger burger) {
            this.burger = burger;
            return this;
        }

        public MealBuilder setDrink(ColdDrink drink) {
            this.drink = drink;
            return this;
        }
    }

    static class Meal {
        private Burger burger;
        private ColdDrink drink;

        public void setBurger(Burger burger) {
            this.burger = burger;
        }


        public void setDrink(ColdDrink drink) {
            this.drink = drink;
        }

        @Override
        public String toString() {
            return "Meal{" +
                    "burger=" + burger +
                    ", drink=" + drink +
                    '}';
        }
    }

    static abstract class Burger {
        private String info;

        abstract void orderBurger();

        @Override
        public String toString() {
            return "Burger{" +
                    "info='" + info + '\'' +
                    '}';
        }
    }

    static abstract class ColdDrink {
        private String info;

        abstract void orderDrink();

        @Override
        public String toString() {
            return "ColdDrink{" +
                    "info='" + info + '\'' +
                    '}';
        }
    }

    static class ChickenBurger extends Burger {
        @Override
        void orderBurger() {
            super.info = "Order Chicken Burger";
        }
    }

    static class VegBurger extends Burger {

        @Override
        void orderBurger() {
            super.info = "Order Veg Burger";
        }
    }

    static class Coke extends ColdDrink {

        @Override
        void orderDrink() {
            super.info = "Order Coke Drink";
        }
    }

    static class Pepsi extends ColdDrink {
        @Override
        void orderDrink() {
            super.info = "Order Pepsi Drink";
        }
    }
}
