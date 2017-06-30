package com.masonliu.arrow.handler;

import com.masonliu.arrow.annotation.OnPostInject;

import java.lang.reflect.Method;

/**
 * Created by liumeng on 16/11/30.
 */

public class OnPostInjectHandler {
    public static void inject(Object target) {
        Method[] methods = target.getClass().getMethods();
        for (Method method : methods) {
            OnPostInject annotation = method.getAnnotation(OnPostInject.class);
            if (annotation != null) {
                try {
                    method.invoke(target);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
