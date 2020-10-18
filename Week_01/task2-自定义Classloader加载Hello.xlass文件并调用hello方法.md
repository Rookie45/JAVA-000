题目：

自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。  

答题如下：

```java
package com.sl.java00.week01;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomClassloader extends ClassLoader {

    private static final String CLASS_NAME = "Hello";

    private static final String METHOD_NAME = "hello";

    private static final String TARGET_FILE = "Hello.xlass";

    public static void main(String[] args) {
        try {
            Class<?> hello = new CustomClassloader().findClass(CLASS_NAME);
            hello.getMethod(METHOD_NAME).invoke(hello.newInstance());
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        byte[] bytes = null;
        try {
            URI uri = classLoader.getResource(TARGET_FILE).toURI();
            Path path = Paths.get(uri);
            bytes = Files.readAllBytes(path);
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        if (null == bytes) {
            throw new ClassNotFoundException("no found Hello.class.");
        }
        return super.defineClass(name, bytes, 0, bytes.length);
    }
}
```

