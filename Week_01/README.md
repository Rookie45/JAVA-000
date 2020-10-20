学习笔记
```java
package java00.day01;

/**
 * java字节码由单字节指令组成
 * 根据指令性质主要分为：
 * 1.栈操作指令
 * 2.程序流程控制指令
 * 3.对象操作指令
 * 4.算术运算与类型转换指令
 */
public class Hello {

    static final long count = 0;

    private double sum = 0.0D;

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public static void main(String[] args) {
        Hello hello = new Hello();
        double sum = hello.getSum();
        for (int i = 6; i > count; i--) {
            if (0 == i % 2) {
                sum += i;
            } else {
                sum *= i;
            }
        }
        hello.setSum(sum);
    }
}
```
对上述代码编译后，对class文件执行`javap -c -verbose Hello.class`，得到对应的字节码，下面对字节码进行解读
```java
public class java00.day01.Hello
  minor version: 0
  major version: 52						  
  flags: ACC_PUBLIC, ACC_SUPER
```

`minor version: 0`和`major version: 52`表达当前java的次版本和主版本，java的版本号从45开始，除1.0和1.1都是使用45.x外,以后每升一个大版本，版本号加1，所以代码运行的环境是1.8.0的java版本，实际环境中使用的是1.8.0_231，匹配上了。`flags: ACC_PUBLIC, ACC_SUPER`表示访问标识为public，`ACC_SUPER`表示允许使用invokespecial字节码指令的新语义。

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

这块表示Hello类的默认构造函数，首先是方法描述符`descriptor: ()V`，表示返回值void，且无参。接着`flags: ACC_PUBLIC`，表示访问标识为public。后面则是函数体内容，首先`stack=3, locals=1, args_size=1`，“stack=3“表示该段code使用到的最大操作数栈，"locals=1"表示局部变量所需空间为1，"args_size=1"表示方法参数个数为1。

```java
         0: aload_0             //表示取LocalVariableTable中slot为0的对象load到操作栈内
         1: invokespecial #1    // Method java/lang/Object."<init>":()V
         4: aload_0
         5: dconst_0            //表示常量0，类型为double
         6: putfield      #2    // Field sum:D，表示给对象字段sum赋值，值为常量池中#2
         9: return
```

接着是方法体内容，每行指令含义如上所示，这块内容的意思是将this推送至栈顶，然后执行该类型的实例化方法，接着再次将this入栈，将常量0也入栈，在栈中执行字段赋值，最后执行返回语句，结束方法。

```
      LineNumberTable:
        line 11: 0             //源码中11行，对应字节码行号为0处
        line 15: 4             //源码中15行，对应字节码行号为4处
```

上面是`LineNumberTable`，它描述源码行号与字节码行号（偏移量）对应关系。

```java
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      10     0  this   Ljava00/day01/Hello; //
```

上面是`LocalVariableTable`，它描述帧栈中局部变量与源码中定义的变量之间的关系。“start” 表示该局部变量在哪一行开始可见，“length”表示可见行数，“Slot”代表所在帧栈位置，“Name”是变量名称，然后Signature是类型签名。



JVM在内存小于1GB以及1GB以上的默认分配情况，是不一样的。

参考
[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

[轻松看懂Java字节码](https://juejin.im/post/6844903588716609543)

[Jvm系列3—字节码指令](http://gityuan.com/2015/10/24/jvm-bytecode-grammar/)

[jvms-putfield](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.putfield)

