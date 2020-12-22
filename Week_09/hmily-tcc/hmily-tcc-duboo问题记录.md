**问题**

```
Caused by: java.lang.IllegalStateException: Serialized class com.sl.java00.homework.hmilytcccommon.dto.TransactionDTO must implement java.io.Serializable
```

**原因**

```
dto对象需要实现序列化接口
```



疑惑

>问题：
>1）在进行货币兑换时，我写的程序在进行一次兑换后，会再次进入B库中进行冻结和扣除，导致最终数据不一致
>2）按理说上面异常，会进入事务的cancel阶段，保证一致性，但程序没有进入
>3）我在confirm和cancel阶段会再调用B的远程服务进行，这种行为感觉不太合适，课上有说try成功了，confirm一定是成功，但我这里调远程服务，那肯定会有失败的风险，不知道怎么改善
>4）对于@HmilyTCC这类TCC的事务注解，看它的例子，无论单纯本地事务，还是分布式事务都可以使用，然后对@HmilyTCC和@Transactional，在本地事务时，就迷糊了，我写的这个作业里两者都用了。
>
>非常希望能得到小鱼老师的解答，再次感谢
