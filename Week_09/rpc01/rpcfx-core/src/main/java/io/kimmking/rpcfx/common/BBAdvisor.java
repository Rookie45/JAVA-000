package io.kimmking.rpcfx.common;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

public class BBAdvisor {
    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments) {
        if (method.getAnnotation(BB.class) != null) {

        }
    }

    @Advice.OnMethodExit
    public static void onMethodExit(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments, @Advice.Return Object ret) {
        if (method.getAnnotation(BB.class) != null) {
        }
    }

    public static void onMethodThrowable() {

    }
}
