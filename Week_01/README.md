学习笔记



JVM基础知识
=========

* java是一种面向对象，静态类型、编译执行，有VM/GC和运行时、跨平台（二进制跨平台）的高级语言
* java将类文件编译成字节码文件，通过类加载器将字节码加载到虚拟机中。

java类与字节码
============

Java bytecode由单个字节指令组成，理论支持256个操作码，实际java只用了200左右。根据指令的性质，大致分下面四类：

1. 栈操作指令，包含与局部变量交互的指令
2. 程序流程控制指令
3. 对象操作指令，包括方法调用指令
4. 算术运算及类型转换指令

可以通过javap -c -verbose Class文件名查看对应的字节码内容，下面字节码几部分简单说明

```java
public class java00.day01.Hello
  minor version: 0
  major version: 52						  
  flags: ACC_PUBLIC, ACC_SUPER
```

`minor version`和`major version`表达当前java版本，java版本从45开始，所以环境中使用的是java8，`flags`表示访问标志，有如下几种取值：

| 标志名称           | 标志值 | 含义                                                         |
| ------------------ | ------ | ------------------------------------------------------------ |
| **ACC_PUBLIC**     | 0x0001 | 是否为Public类型                                             |
| **ACC_FINAL**      | 0x0010 | 是否被声明为final，只有类可以设置                            |
| **ACC_SUPER**      | 0x0020 | 是否允许使用invokespecial字节码指令的新语义                  |
| **ACC_INTERFACE**  | 0x0200 | 标志这是一个接口                                             |
| **ACC_ABSTRACT**   | 0x0400 | 是否为abstract类型，对于接口或者抽象类来说，次标志值为真，其他类型为假 |
| **ACC_SYNTHETIC**  | 0x1000 | 标志这个类并非由用户代码产生                                 |
| **ACC_ANNOTATION** | 0x2000 | 标志这是一个注解                                             |
| **ACC_ENUM**       | 0x4000 | 标志这是一个枚举                                             |

接着是常量池，可以理解为资源库，主要放字面量和符号引用，字面量类似java中的常量，如文本字符串等，而符号引用属于编译原理方面的概念，包含以下三种：

* 类和接口的全限定名（类似#7）
* 字段的名称和描述符号（类似#2）
* 方法的名称和描述符（类似#1）

```java
Constant pool:
   #1 = Methodref          #7.#37         // java/lang/Object."<init>":()V
   #2 = Fieldref           #3.#38         // java00/day01/Hello.sum:D
   #3 = Class              #39            // java00/day01/Hello
   ...
   #7 = Class              #42            // java/lang/Object
   #8 = Utf8               count
   #9 = Utf8               J
   ...
  #37 = NameAndType        #15:#16        // "<init>":()V
  #38 = NameAndType        #13:#14        // sum:D
  #39 = Utf8               java00/day01/Hello
   ...
  #42 = Utf8               java/lang/Object
```

`#1 = Methodref  #7.#37`这里定义方法，值为“#7.#37”，也就是“第7个常量**.**第37个常量”，“#7”的值为`#7 = Class  #42`，它定义class，class为“#42”，“#42”的值为`#42 = Utf8  java/lang/Object`，即“#7”表示为类“java/lang/Object”，同理可以得出”#37表示为方法"<init>":()V，到此，可以得出“#1”最终表示的是“java/lang/Object."<init>":()V”，就是Object的默认构造函数。这里对某些符号表示的类型进行说明：

| 标识字符 | 含义                                      |
| -------- | ----------------------------------------- |
| B        | 基本类型byte                              |
| C        | 基本类型char                              |
| D        | 基本类型double                            |
| F        | 基本类型float                             |
| I        | 基本类型int                               |
| J        | 基本类型long                              |
| S        | 基本类型short                             |
| Z        | 基本类型boolean                           |
| V        | 特殊类型void                              |
| L        | 对象类型，以分号结尾，如Ljava/lang/Object |

```java
  static final long count;
    descriptor: J
    flags: ACC_STATIC, ACC_FINAL
    ConstantValue: long 0l
```

这块对应到代码中的成员变量`static final long count = 0;`，首先`descriptor : J`，描述字段类型为long，接着`flags: ACC_STATIC, ACC_FINAL`，描述字段访问标识为staic, final，`ConstantValue: long 0l`描述字段的值为常量0

```java
  public java00.day01.Hello();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=1, args_size=1
         0: aload_0            //表示取LocalVariableTable中slot为0的对象load到操作栈内
         1: invokespecial #1   // Method java/lang/Object."<init>":()V
         4: aload_0
         5: dconst_0           //表示常量0，类型为double
         6: putfield      #2   // Field sum:D，表示给对象字段sum赋值，值为常量池中#2
         9: return
      LineNumberTable:
        line 11: 0            //源码中11行，对应字节码行号为0处
        line 15: 4            //源码中15行，对应字节码行号为4处
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      10     0  this   Ljava00/day01/Hello;//局部变量this
```

这块表示Hello类的默认构造函数，首先是方法描述符`descriptor: ()V`，表示返回值void，且无参。接着`flags: ACC_PUBLIC`，表示访问标识为public。后面则是函数体内容，首先`stack=3, locals=1, args_size=1`，“stack=3“表示该段code使用到的最大操作数栈，"locals=1"表示局部变量所需空间为1，"args_size=1"表示方法参数个数为1。

`code`函数体内容，每行指令含义如上所示，这块内容的意思是将this推送至栈顶，然后执行该类型的实例化方法，接着再次将this入栈，将常量0也入栈，在栈中执行字段赋值，最后执行返回语句，结束方法。这里对方法调用的指令进行说明：

| 指令            | 含义                                                         |
| --------------- | ------------------------------------------------------------ |
| invokestatic    | 用于调用某个类的静态方法，在方法调用指令中最快               |
| invokespecial   | 用于调用构造函数，类中的private以及超类中可见的方法          |
| invokevirtual   | 如果是具体类型对象，可用于调用public，protected以及package级别的方法 |
| invokeinterface | 通过接口引用来调用方法                                       |
| invokedynamic   | JDK7新增指令，用于实现“动态类型语言”的支持                   |

`LineNumberTable`描述源码行号与字节码行号（偏移量）对应关系。

`LocalVariableTable`描述帧栈中局部变量与源码中定义的变量之间的关系。“start” 表示该局部变量在哪一行开始可见，“length”表示可见行数，“Slot”代表所在帧栈位置（它是局部变量表最基础的存储单位，32位以内类型占一个slot），“Name”是变量名称，然后“Signature”是类型签名。

总体来说，变量从局部变量表中load到操作栈中进行操作计算，将中间结果store到局部变量表，这期间会用Constant Pool进行一些值的查找，在所有操作计算结束后，弹出栈返回。



JVM类加载器
===========

JVM的类加载器有三类：

1. 启动类加载器（BootstrapClassLoader）
2. 扩展类加载器（ExtClassLoader）
3. 应用类加载器（AppClassLoader）

加载顺序，从上至下，其中启动类加载器不是有java实现，是看不到的，核心jar包rt.jar的加载由启动类加载器加载，扩展类加载器加载ext文件夹下的jar，应用类加载器则最接近开发，自定义类加载器一般都是在应用类加载器之后。

加载器的特性：1）双亲委托，2）负责依赖，3）缓存加载。这里说一下双亲委托，意思是遇到一个类加载，需要先问过父加载器（顺序越靠前，辈分越高），父加载器会再往上，祖父类加载器，直到启动类加载器，当前辈们都没有这个类，则自己才可以加载。

**类的生命周期包含7个步骤**：

1. 加载（loading），找class文件
2. 验证（verification），验证格式，依赖
3. 准备（preparation），静态字段，方法表
4. 解析（resolution），符号解析为引用
5. 初始化（initialization），构造器，静态变量赋值，静态代码块
6. 使用（using）
7. 卸载（unloading）

中间的2，3，4又可以合并称为链接（linking）

**类加载初始化的时机有如下几种情况**：

1. 当虚拟机启动时，初始化用户指定主类（main方法所在类）
2. 当使用new指令新建目标类实例时，初始化new指令的目标类（new对象）
3. 当调用静态方法指令时，初始化该静态方法所在类
4. 当访问静态字段时，初始化该静态字段所在类
5. 子类初始化会触发父类初始化
6. 接口定义了default方法，直接实现或者间接实现该接口的类的初始化会让该接口初始化
7. 使用反射API对某个类进行反射调用时，初始化该类（类似2，3）
8. 初次调用MethodHandle实例时，初始化MethodHandle指向的方法所在类

**类不会初始化有如下几种情况：**

1. 通过子类引用父类的静态字段，只会触发父类初始化，并不会触发子类初始化
2. 定义对象数组，不会触发该类的初始化
3. 常量在编译期间存入调用类的常量池，本质上并没有直接引用定义常量的类，也就不会触发该类的初始化
4. 通过类名获取Class对象，不会触发类的初始化
5. 通过Class.forName加载指定类时，如果指定参数initialize为false，不会触发该类初始化
6. 通过ClassLoader默认的loadClass方法，不会触发初始化动作



JVM内存模型
===========

JVM内存模型将内存分为线程栈和堆，线程栈存储所有局部原始类型（8大基础类型），不同线程的这些局部变量相互之间不可见。线程之间是以复制变量副本传递，而非变量本身；堆存储所有线程创建的对象，包括包装类，另外创建的对象赋值给局部变量，该对象还是存储在堆里，而栈中仅存储对象的引用

非堆：meta（方法区，常量池），ccs，code cache；堆外：直接内存，绕过JVM使用的内存

JVM在执行一段程序时，会在内存定义不同的run-time数据区，这些数据区有的和JVM生命周期一样长，有的和创建的线程生命周期一样长。数据区包含以下几种

PC寄存器（program counter register）：每个JVM线程都有自己的PC寄存器，线程执行的当前方法，如果不是native方法，则PC寄存器存储着当前正在执行的JVM指令的地址，如果是native方法，则PC寄存器的值是undefined

JVM栈：每个JVM线程都有自己的JVM栈，它存储着栈帧，包含的内容有局部变量和部分返回结果，JVM规范允许创建时固定栈的大小，也可以指定栈的范围，让其动态伸缩。

堆：所有java线程共享，用于存储类的实例和数组，由GC管理，同样可以固定大小，也可以动态伸缩

方法区：所有java线程共享，用于存储每个类的结构，如运行时常量池、字段和方法数据，以及方法和构造函数的代码，包括类和实例初始化以及接口初始化中使用的特殊方法。方法区在JVM启动时创建，尽管逻辑上它是堆的一部分，但可以不进行GC或压缩。同样可以固定大小，也可以动态伸缩。

运行时常量池：每个类或者接口中的Constant Pool Table，主要包含（编译时）数值字面值，（运行时必须的）方法和字段引用。每个运行时常量池都是从JVM的方法区分配，类和接口的运行时常量池在JVM创建类和接口时构建。

java8之后，meta区代替了永久代，常量池在 Meta区， 静态变量本身的存储区域在非堆中，当然也可能是指针，指向其他区域。 Meta区这些概念是Hotspot具体实现的，学习时注意规范与具体实现的区别， JVM规范并没有规定说你必须实现哪个区。



JVM启动参数
==========

Xss如果不显示设置，在64位Linux上HotSpot的线程栈容量默认是1MB，此外内核数据结构还会额外消耗16KB。而一个协程的栈通常在几百字节到几KB之间，差了1个数量级。

Xms（memory start size）和Xmx（memory max size）设置时（单位Byte），可以配置-XX:+HeapDumpOnOutOfMemoryError参数，让虚拟机在出现内存溢出时Dump出当前的内存堆转储快照，以便事后分析。那推荐-XX:+HeapDumpOnOutOfMemoryError在线上环境配置吗？另外关于Xms，经验值一般设置为当前系统内存的70%。

metaspace代替了永久代，存储一些类型信息，比如静态变量和方法，与Compressed Class Space有交叉

DirectMemory属于堆外空间，容量大小通过-XX:MaxDirectMemorySize指定，如果不指定，默认与堆（-Xmx的值）一致。

非堆内存与堆外内存的区别：非堆内存依然归JVM管理，但业务代码无法涉及到，堆外内存一般是为了绕开GC直接使用的内存，业务代码特别是JNI用，通常情况下可能是非Java语言或者DirectBuffer使用。


JDK内置命令行工具与图形化工具
==================



工具or命令|简介
------------ | -------------
jps/jinfo|查看java进程
jstat|查看JVM内存gc的相关信息，jstat -gc pid 收集时间间隔(ms) 收集次数
jmap|查看heap或类占用空间，jmap -heap pid
jstack|查看线程信息 ，jstack -l pid
jcmd|执行JVM相关分析命令（整合性的命令），jcmd pid 参数
jrunscript/jjs|执行js命令
jsconsole工具| 查看堆内存使用量，线程，类，CPU占用率 
jvisualvm工具| 同样可以查看堆，线程，类，CPU占用率，另外有快照功能，进行瞬时分析 
visualGC工具|主要对堆和GC情况进行详细展示，可以在idea以插件形式安装
jmc工具|对应用进行管理，监控，分析以及故障排查的工具套件，很强


GC的背景与一般原理
==========

源于内存资源有限，资源的回收与再利用是必然的，那么回收的方式可以手动，也可以专门程序来做。回收的实现方式，有引用计数，但由于它无法解决环型引用，因为这种情况，引用的计数是永不为0的，这就造成内存泄漏（内存无法回收），如果这种情况再多点，就造成内存溢出（无可用内存）。由此，有了引用跟踪，即由某几个存活对象开始，标记与它有关联的对象，未被标记的对象，即为可回收对象，这样一来能解决环形引用，二来不用扫描所有对象。

各个GC的简介
==========


收集器|串行/并行/并发|新生代/老年代|收集算法|目标|适用场景
------------ | -------------| -------------| -------------| -------------| -------------
Serial|串行|新生代|复制算法|响应优先|单CPU下的Client模式
Serial Old|串行|老年代|标记整理|响应优先|单CPU下的Client模式，CMS后备预案
ParNew|并行|新生代|复制算法|响应优先|多CPU下的Server模式，与CMS配合
Parallel Scavenge|并行|新生代|复制算法|吞吐量优先|在后台运算而不需要太多交互的任务
Parallel Old|并行|老年代|标记整理|吞吐量优先|在后台运算而不需要太多交互的任务
CMS|并发|老年代|标记清除|响应优先|集中在互联网站或B/S系统服务端上的java应用
G1|并发|both|标记整理+复制算法|响应优先|面向服务端应用，未来替换CMS




参考

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

[轻松看懂Java字节码](https://juejin.im/post/6844903588716609543)

[Jvm系列3—字节码指令](http://gityuan.com/2015/10/24/jvm-bytecode-grammar/)

[jvms-putfield](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.putfield)

