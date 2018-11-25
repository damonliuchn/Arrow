package com.masonliu.arrow.handler;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

import com.masonliu.arrow.annotation.InjectView;
import com.masonliu.arrow.model.FieldInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.masonliu.arrow.handler.InjectFieldHandler.fieldInfosMap;
import static com.masonliu.arrow.handler.InjectFieldHandler.getFieldInfos;

/**
 * Created by liumeng on 16/11/30.
 */

public class InjectViewHandler {

    public static void inject(Object target) {
        //target.getClass() 是单例，fieldInfosMap.containsKey 判断的是hashcode
        if (!fieldInfosMap.containsKey(target.getClass())) {
            fieldInfosMap.put(target.getClass(), getFieldInfos(target.getClass()));
        }
        for (FieldInfo fieldInfo : fieldInfosMap.get(target.getClass())) {
            Field field = fieldInfo.getField();
            if (field.isAnnotationPresent(InjectView.class)) {
                setField(target, field);
            }
        }
    }

    private static void setField(Object target, Field field) {
        try {
            InjectView annotation = field.getAnnotation(InjectView.class);
            View view = null;
            if (target instanceof Activity) {
                Activity activity = (Activity) target;
                view = activity.findViewById(annotation.value());
            } else if (target instanceof Fragment) {
                Fragment fragment = (Fragment) target;
                view = fragment.getView().findViewById(annotation.value());
            } else {
                Class supportFragment = Class.forName("android.support.v4.app.Fragment");
                if (supportFragment.isAssignableFrom(target.getClass())) {
                    Method getView = supportFragment.getMethod("getView");
                    View viewGroup = (View) getView.invoke(target);
                    view = viewGroup.findViewById(annotation.value());
                }
            }
            field.setAccessible(true);
            field.set(target, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
