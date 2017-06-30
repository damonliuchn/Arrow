package com.masonliu.arrow.model;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import javax.inject.Provider;

public class FieldInfo {
    private Class clazz;
    private Field field;
    private boolean isProviderType;
    /**
     *  classInfo 始终是最真实需要的类型，会自动透过Provider那一层
     */
    private ClassInfo classInfo;

    public FieldInfo(Field field) {
        Class providerType = field.getType().equals(Provider.class) ?
                (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0] :
                null;
        isProviderType = providerType != null;
        clazz = isProviderType ?
                providerType :
                field.getType();
        classInfo = new ClassInfo(clazz);
        this.field = field;
    }


    public Field getField() {
        return field;
    }

    public boolean isProviderType() {
        return isProviderType;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }
}