学习笔记

```java
public class java00.day01.Hello
  minor version: 0
  major version: 52						  
  flags: ACC_PUBLIC, ACC_SUPER
```

`minor version`和`major version`表达当前java小版本，例如环境中使用的是1.8_52_0，`ACC_PUBLIC`访问修饰符为public，`ACC_SUPER`访问父类？

```java
Constant pool:
   #1 = Methodref          #7.#37         // java/lang/Object."<init>":()V
   #2 = Fieldref           #3.#38         // java00/day01/Hello.sum:D
   #3 = Class              #39            // java00/day01/Hello
   #4 = Methodref          #3.#37         // java00/day01/Hello."<init>":()V
   #5 = Methodref          #3.#40         // java00/day01/Hello.getSum:()D
   #6 = Methodref          #3.#41         // java00/day01/Hello.setSum:(D)V
   #7 = Class              #42            // java/lang/Object
   #8 = Utf8               count
   #9 = Utf8               J
  #10 = Utf8               ConstantValue
  #11 = Long               0l
  #13 = Utf8               sum
  #14 = Utf8               D
  #15 = Utf8               <init>
  #16 = Utf8               ()V
  #17 = Utf8               Code
  #18 = Utf8               LineNumberTable
  #19 = Utf8               LocalVariableTable
  #20 = Utf8               this
  #21 = Utf8               Ljava00/day01/Hello;
  #22 = Utf8               getSum
  #23 = Utf8               ()D
  #24 = Utf8               setSum
  #25 = Utf8               (D)V
  #26 = Utf8               main
  #27 = Utf8               ([Ljava/lang/String;)V
  #28 = Utf8               i
  #29 = Utf8               I
  #30 = Utf8               args
  #31 = Utf8               [Ljava/lang/String;
  #32 = Utf8               hello
  #33 = Utf8               StackMapTable
  #34 = Class              #39            // java00/day01/Hello
  #35 = Utf8               SourceFile
  #36 = Utf8               Hello.java
  #37 = NameAndType        #15:#16        // "<init>":()V
  #38 = NameAndType        #13:#14        // sum:D
  #39 = Utf8               java00/day01/Hello
  #40 = NameAndType        #22:#23        // getSum:()D
  #41 = NameAndType        #24:#25        // setSum:(D)V
  #42 = Utf8               java/lang/Object
```

以上是常量池，数值超出`[-128, 127]`即会放入常量池中？`#1 = Methodref  #7.#37`这里=表示定义？常量池第一个位置定义方法引用，方法引用为“#7.#37”，也就是“第7个常量**.**第37个常量”，“#7”的值为`#7 = Class  #42`，它定义class，class为“#42”，“#42”的值为`#42 = Utf8  java/lang/Object`，即“#7”表示为类“java/lang/Object”，同理可以得出”#37表示为方法"<init>":()V，到此，可以得出“#1”最终表示的是“java/lang/Object."<init>":()V”，就是Object的默认构造函数。

```java
  static final long count;
    descriptor: J
    flags: ACC_STATIC, ACC_FINAL
    ConstantValue: long 0l
```

这块对应到代码中的`static final long count = 0;`，首先`descriptor : J`，描述字段类型为long，其中“J”为long类型的标识字符，接着`flags: ACC_STATIC, ACC_FINAL`，描述字段访问标识为staic, final，`ConstantValue: long 0l`字段的值常量为0l

```java
  public java00.day01.Hello();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: dconst_0
         6: putfield      #2                  // Field sum:D
         9: return
      LineNumberTable:
        line 11: 0
        line 15: 4
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      10     0  this   Ljava00/day01/Hello;
```

这块表示Hello类的默认构造函数，首先是方法描述符`descriptor: ()V`，表示返回值void，且无参。接着`flags: ACC_PUBLIC`，表示访问标识为public。后面则是函数体内容，首先`stack=3, locals=1, args_size=1`，“stack=3“表示该段code使用到的最大操作数栈，"locals=1"表示局部变量所需空间，参数



参考

[轻松看懂Java字节码](https://juejin.im/post/6844903588716609543)

