package com.sl.homework.week0601.designpattern;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * 原型模式，创建重复对象，个人感觉可以理解为对象的深拷贝
 * 提前创建好需要复制的对象，后面需要的时候，复制已创建的模子，省了new的过程
 * 和工厂模式搭配不错，将工厂方法new的方式换成原型里的clone，前提对象实现了Cloneable接口
 */
public class PrototypeDemo {
    public static void main(String[] args) {
        ShapeCache.initCache();
        for (int i = 0; i < 5; i++) {
            Shape circle = ShapeCache.getShape("Circle");
            System.out.println(circle.hashCode() + ":" + circle);
            Shape square = ShapeCache.getShape("Square");
            System.out.println(square.hashCode() + ":" + square);
        }
    }

    static class ShapeCache {
        private static Hashtable<String, Shape> shapeMap = new Hashtable<>();

        public static Shape getShape(String shapeId) {
            return (Shape) shapeMap.get(shapeId).clone();
        }

        public static void initCache() {
            Circle circle = new Circle();
            circle.setId("Circle");
            circle.setInfos(Arrays.asList("center", "radius"));
            shapeMap.put("Circle", circle);
            Square square = new Square();
            square.setId("Square");
            square.setInfos(Arrays.asList("side", "wide"));
            shapeMap.put("Square", square);
        }
    }

    static abstract class Shape implements Cloneable {
        private String id;
        private List<String> infos;

        abstract void draw();

        public Shape clone() {
            Shape shapeClone = null;
            try {
                shapeClone = (Shape) super.clone();
                shapeClone.infos = deepCopy(this.infos);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            return shapeClone;
        }

        private List<String> deepCopy(List<String> src) {
            List<String> dest = null;
            ByteArrayInputStream byteIn = null;
            ObjectInputStream in = null;

            try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                 ObjectOutputStream out = new ObjectOutputStream(byteOut);) {
                out.writeObject(src);
                byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                in = new ObjectInputStream(byteIn);
                dest = (List<String>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (null != byteIn) {
                    try {
                        byteIn.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return dest;
        }


        public void setId(String id) {
            this.id = id;
        }


        public void setInfos(List<String> infos) {
            this.infos = infos;
        }

        @Override
        public String toString() {
            return "Shape{" +
                    "id='" + id + '\'' +
                    ", infos=" + infos +
                    '}';
        }
    }

    static class Circle extends Shape {

        @Override
        public void draw() {
            System.out.println("Draw Circle...");
        }
    }

    static class Square extends Shape {

        @Override
        public void draw() {
            System.out.println("Draw Square...");
        }
    }

}
