**题目**：

自己写一个简单的 Hello.java，里面需要涉及基本类型，四则运行，if 和 for，然后自己分析一下对应的字节码  

**答题如下**：

自定义类

```java
package com.sl.java00.day01;

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

**对应的字节码解读**

```java
Classfile /E:/Projects/thinkinjava/target/classes/com/sl/java00/day01/Hello.class
  Last modified 2020-10-17; size 858 bytes
  MD5 checksum 17ed6691ab7644480986bf4678fef58c
  Compiled from "Hello.java"
public class com.sl.java00.day01.Hello
  minor version: 0                   //当前java的次版本
  major version: 52                  //当前java的主版本，java版本从45开始
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #7.#37         // java/lang/Object."<init>":()V
   #2 = Fieldref           #3.#38         // com/sl/java00/day01/Hello.sum:D
   #3 = Class              #39            // com/sl/java00/day01/Hello
   #4 = Methodref          #3.#37         // com/sl/java00/day01/Hello."<init>":()V
   #5 = Methodref          #3.#40         // com/sl/java00/day01/Hello.getSum:()D
   #6 = Methodref          #3.#41         // com/sl/java00/day01/Hello.setSum:(D)V
   #7 = Class              #42            // java/lang/Object
   #8 = Utf8               count
   #9 = Utf8               J             //long
  #10 = Utf8               ConstantValue
  #11 = Long               0l
  #13 = Utf8               sum
  #14 = Utf8               D             //double
  #15 = Utf8               <init>
  #16 = Utf8               ()V
  #17 = Utf8               Code
  #18 = Utf8               LineNumberTable
  #19 = Utf8               LocalVariableTable
  #20 = Utf8               this
  #21 = Utf8               Lcom/sl/java00/day01/Hello;
  #22 = Utf8               getSum
  #23 = Utf8               ()D
  #24 = Utf8               setSum
  #25 = Utf8               (D)V
  #26 = Utf8               main
  #27 = Utf8               ([Ljava/lang/String;)V  //void (String[])
  #28 = Utf8               i
  #29 = Utf8               I
  #30 = Utf8               args
  #31 = Utf8               [Ljava/lang/String; //String[]数组
  #32 = Utf8               hello
  #33 = Utf8               StackMapTable
  #34 = Class              #39            // com/sl/java00/day01/Hello
  #35 = Utf8               SourceFile
  #36 = Utf8               Hello.java
  #37 = NameAndType        #15:#16        // "<init>":()V
  #38 = NameAndType        #13:#14        // sum:D
  #39 = Utf8               com/sl/java00/day01/Hello
  #40 = NameAndType        #22:#23        // getSum:()D
  #41 = NameAndType        #24:#25        // setSum:(D)V
  #42 = Utf8               java/lang/Object
{
  static final long count;
    descriptor: J                //描述字段类型为long
    flags: ACC_STATIC, ACC_FINAL //描述字段访问标识为staic, final
    ConstantValue: long 0l       //字段的值常量为long类型0

  public java00.day01.Hello();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=1, args_size=1
         0: aload_0             //表示取LocalVariableTable中slot为0的对象load到操作栈内
         1: invokespecial #1    // Method java/lang/Object."<init>":()V
         4: aload_0             //表示将局部变量表中slot为0的对象变量load到操作栈中
         5: dconst_0            //表示常量0，类型为double
         6: putfield      #2    // Field sum:D，表示给对象字段sum赋值，值为常量池中#2
         9: return
      LineNumberTable:
        line 11: 0             //源码中11行，对应字节码行号为0处
        line 15: 4             //源码中15行，对应字节码行号为4处
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      10     0  this   Lcom/sl/java00/day01/Hello;//L表示对象类型

  public double getSum();
    descriptor: ()D                    //无参，返回值为double
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: getfield      #2          // Field sum:D 获取字段sum
         4: dreturn                   // 返回double类型
      LineNumberTable:
        line 18: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/sl/java00/day01/Hello;

  public void setSum(double);
    descriptor: (D)V                   //一个double入参，返回值为void
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=3, args_size=2  //栈深3，this，对象字段，入参value
         0: aload_0
         1: dload_1
         2: putfield      #2          // Field sum:D 给对象字段sum赋值，值为常量池中#2
         5: return
      LineNumberTable:
        line 22: 0
        line 23: 5
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       6     0  this   Lcom/sl/java00/day01/Hello;
            0       6     1   sum   D

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V    //入参为String类型数组，返回void
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=4, locals=5, args_size=1
         0: new           #3            //class com/sl/java00/day01/Hello，创建hello
         3: dup                         //复制栈顶元素hello
         4: invokespecial #4            //Method "<init>":()V，弹出hello，初始化对象
         7: astore_1                    //将栈顶对象存入局部变量hello
         8: aload_1                     //将hello对象加载到栈中
         9: invokevirtual #5            //Method getSum:()D，弹出hello，调用getsum
        12: dstore_2                    //取栈顶double值存入局部变量sum
        13: bipush        6             //将6入栈
        15: istore        4             //将栈顶元素存入局部变量i
        17: iload         4             //将局部变量i加载到栈顶
        19: i2l                         //将i从int转为long
        20: lconst_0                    //常数0入栈
        21: lcmp                        //栈中i和0出栈作比较，返回-1/0/1
        22: ifle          54            //将栈顶元素出栈与0比较，小于等于0则移动至54处执行
        25: iconst_0                    //常量0入栈
        26: iload         4             //加载局部变量i到栈顶
        28: iconst_2                    //将2入栈
        29: irem                        //将栈顶两元素出栈取模运算，值入栈
        30: if_icmpne     42            //将栈顶两元素出栈比较，不等则移动至42处执行
        33: dload_2                     //将局部变量sum入栈
        34: iload         4             //将i入栈
        36: i2d                         //将i从int转为long
        37: dadd                        //将sum和i相加，值入栈
        38: dstore_2                    //将栈顶元素存入局部变量sum
        39: goto          48            //跳转至48
        42: dload_2                     //将局部变量sum入栈
        43: iload         4             //将i入栈
        45: i2d                         //将i从int转为long
        46: dmul                        //将sum和i相乘，值入栈
        47: dstore_2                    //将栈顶元素存入局部变量sum
        48: iinc          4, -1         //将局部变量i增加-1，也就是减1
        51: goto          17            //跳转至17
        54: aload_1                     //将hello加载到栈中
        55: dload_2                     //将sum加载到栈中
        56: invokevirtual #6            // Method setSum:(D)V，弹出hello调用setsum
        59: return                      //返回结束
      LineNumberTable:
        line 26: 0
        line 27: 8
        line 28: 13
        line 29: 25
        line 30: 33
        line 32: 42
        line 28: 48
        line 35: 54
        line 36: 59
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
           17      37     4     i   I
            0      60     0  args   [Ljava/lang/String;
            8      52     1 hello   Lcom/sl/java00/day01/Hello;
           13      47     2   sum   D
      StackMapTable: number_of_entries = 4  //用于验证阶段，类型检查验证器使用
        frame_type = 254 /* append */
          offset_delta = 17
          locals = [ class com/sl/java00/day01/Hello, double, int ]
        frame_type = 24 /* same */
        frame_type = 5 /* same */
        frame_type = 250 /* chop */
          offset_delta = 5
}
SourceFile: "Hello.java"

```

