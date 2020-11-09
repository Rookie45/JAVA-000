**题目：**
思考有多少种方式，在main函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？


**答题如下**：

```java
    //1. 创建线程和静态变量，将计算结果存入静态变量
    private static void method1() {
        long start = System.currentTimeMillis();
        int result = -1;
        // 在这里创建一个线程或线程池，
        Thread thread = new Thread(() -> {
            resultWithMethod = sum();
        });
        // 异步执行 下面方法
        thread.start();
        try {
            thread.join();
            result = resultWithMethod;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

```

参考

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

