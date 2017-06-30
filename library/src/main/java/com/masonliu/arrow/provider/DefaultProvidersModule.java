package com.masonliu.arrow.provider;

import android.app.Application;

import com.masonliu.arrow.Arrow;
import com.masonliu.arrow.model.ClassInfo;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Provider;

/**
 * Created by liumeng on 16/12/2.
 */

public class DefaultProvidersModule {
    public static Provider<Application> getApplication() {
        return new Provider<Application>() {
            @Override
            public Application get() {
                return Arrow.getApplication();
            }
        };
    }

    public static Method getMethod(ClassInfo classInfo) {
        Method[] methods = DefaultProvidersModule.class.getDeclaredMethods();
        for (Method method : methods) {
            Type returnType = method.getGenericReturnType();
            if (returnType.equals(Method.class)) {
                continue;
            }
            Class actualClass = (Class) ((ParameterizedType) returnType).getActualTypeArguments()[0];
            if (actualClass.equals(classInfo.getClazz())) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }
}
