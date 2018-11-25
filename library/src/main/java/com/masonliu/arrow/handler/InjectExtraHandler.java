package com.masonliu.arrow.handler;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.masonliu.arrow.annotation.InjectExtra;
import com.masonliu.arrow.model.FieldInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.masonliu.arrow.handler.InjectFieldHandler.fieldInfosMap;
import static com.masonliu.arrow.handler.InjectFieldHandler.getFieldInfos;

/**
 * Created by liumeng on 16/11/30.
 */

public class InjectExtraHandler {

    public static void inject(Object target) {
        //target.getClass() 是单例，fieldInfosMap.containsKey 判断的是hashcode
        if (!fieldInfosMap.containsKey(target.getClass())) {
            fieldInfosMap.put(target.getClass(), getFieldInfos(target.getClass()));
        }
        for (FieldInfo fieldInfo : fieldInfosMap.get(target.getClass())) {
            Field field = fieldInfo.getField();
            if (field.isAnnotationPresent(InjectExtra.class)) {
                try {
                    Bundle bundle = null;
                    if (target instanceof Activity) {
                        Activity activity = (Activity) target;
                        bundle = activity.getIntent().getExtras();
                    } else if (target instanceof Fragment) {
                        Fragment fragment = (Fragment) target;
                        bundle = fragment.getArguments();
                    } else {
                        try {
                            Class supportFragment = Class.forName("android.support.v4.app.Fragment");
                            if (supportFragment.isAssignableFrom(target.getClass())) {
                                Method getView = supportFragment.getMethod("getArguments");
                                bundle = (Bundle) getView.invoke(target);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    setField(bundle, target, field);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static void setField(Bundle bundle, Object target, Field field) {
        try {
            if (bundle == null) {
                return;
            }
            InjectExtra annotation = field.getAnnotation(InjectExtra.class);
            String key = annotation.value();
            if (!bundle.containsKey(key)) {
                return;
            }
            Object value = bundle.get(key);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
