package com.masonliu.arrow.model;

import java.lang.reflect.Constructor;

import javax.inject.Inject;

/**
 * Created by liumeng on 16/12/6.
 */

public class ClassInfo {
    private Class clazz;
    private Constructor constructor;

    public ClassInfo(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    public Constructor getConstructor() {
        if (constructor != null) {
            return constructor;
        }
        //因为有很多构造函数，Arrow不知道选择哪一个，需要用Inject来标记告诉Arrow
        //优先使用inject标记的 否则使用 无参数构造方法，否则使用第一个构造方法
        Constructor inject = null;
        Constructor noarg = null;
        Constructor first = null;
        for (Constructor c : clazz.getDeclaredConstructors()) {
            if (first == null) {
                first = c;
            }
            if (c.isAnnotationPresent(Inject.class)) {
                if (inject == null) {
                    inject = c;
                }
            } else if (c.getParameterTypes().length == 0) {
                noarg = c;
            }
        }
        constructor = inject != null ? inject : noarg;
        constructor = constructor == null ? first : constructor;
        if (constructor != null) {
            constructor.setAccessible(true);
            return constructor;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ClassInfo)) {
            return false;
        }
        ClassInfo classInfo = (ClassInfo) obj;
        if (classInfo.getClazz().getName().equals(clazz.getName())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return clazz.getName().hashCode();
    }
}
