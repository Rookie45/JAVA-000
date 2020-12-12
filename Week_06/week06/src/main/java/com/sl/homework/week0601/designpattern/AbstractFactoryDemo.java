package com.sl.homework.week0601.designpattern;

/**
 * 抽象工厂，工厂类的工厂，在一个工厂里聚合多个同类产品，比如下面的Shape和Color
 * 这里抽象工厂 + 泛型 + 反射，一定程度上能够减少扩展功能时，修改代码范围。
 * 但还是需要，AbstractFactory扩展新工厂方法时，其他原本的工厂都需要实现一个空方法，这点很恶心
 */
public class AbstractFactoryDemo {
    public static void main(String[] args) {
        ShapeFactory shapeFactory = FactoryBuilder.getFactory(ShapeFactory.class);
        Circle circle = shapeFactory.getShape(Circle.class);
        circle.draw();
        Rectangle rectangle = shapeFactory.getShape(Rectangle.class);
        rectangle.draw();
        SexFactory sexFactory = FactoryBuilder.getFactory(SexFactory.class);
        Man man = sexFactory.getSex(Man.class);
        man.say();
    }

    static class FactoryBuilder {
        public static <T extends AbstractFactory> T getFactory(Class<T> aClass) {
            try {
                return aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    static abstract class AbstractFactory {

        public abstract <T extends Shape> T getShape(Class<T> sClass);

        public abstract <T extends Color> T getColor(Class<T> sClass);

        public abstract <T extends Sex> T getSex (Class<T> sClass);
    }

    static class ShapeFactory extends AbstractFactory {

        @Override
        public <T extends Shape> T getShape(Class<T> sClass) {
            try {
                return sClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public <T extends Color> T getColor(Class<T> sClass) {
            return null;
        }

        @Override
        public <T extends Sex> T getSex(Class<T> sClass) {
            return null;
        }
    }

    static class ColorFactory extends AbstractFactory {

        @Override
        public <T extends Shape> T getShape(Class<T> sClass) {
            return null;
        }

        @Override
        public <T extends Color> T getColor(Class<T> sClass) {
            try {
                return sClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public <T extends Sex> T getSex(Class<T> sClass) {
            return null;
        }
    }

    static class SexFactory extends AbstractFactory {
        @Override
        public <T extends Shape> T getShape(Class<T> sClass) {
            return null;
        }

        @Override
        public <T extends Color> T getColor(Class<T> sClass) {
            return null;
        }

        @Override
        public <T extends Sex> T getSex(Class<T> sClass) {
            try {
                return sClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    interface Sex {
        void say();
    }

    static class Man implements Sex {
        @Override
        public void say() {
            System.out.println("this is man...");
        }
    }

    static class Woman implements Sex {
        @Override
        public void say() {
            System.out.println("this is Woman...");
        }
    }

    interface Shape {
        void draw();
    }

    static class Circle implements Shape {
        public Circle() {
        }

        @Override
        public void draw() {
            System.out.println("Draw Circle...");
        }
    }

    static class Square implements Shape {
        public Square() {
        }

        @Override
        public void draw() {
            System.out.println("Draw Square...");
        }
    }

    static class Rectangle implements Shape {
        @Override
        public void draw() {
            System.out.println("Draw Rectangle...");
        }
    }

    interface Color {
        void fill();
    }

    static class Black implements Color {
        public Black() {
        }

        @Override
        public void fill() {
            System.out.println("Fill Black...");
        }
    }

    static class White implements Color {
        public White() {
        }

        @Override
        public void fill() {
            System.out.println("Fill White...");
        }
    }
}
