package com.masonliu.arrow.handler;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

import com.masonliu.arrow.annotation.OnClick;

import java.lang.reflect.Method;

/**
 * Created by liumeng on 16/11/30.
 */

public class OnClickHandler {
    public static void inject(Object target) {
        Method[] methods = target.getClass().getMethods();
        for (Method method : methods) {
            OnClick annotation = method.getAnnotation(OnClick.class);
            if (annotation != null) {
                callOnClickMethod(target, annotation, method);
            }
        }
    }

    private static void callOnClickMethod(final Object target, OnClick annotation, final Method method) {
        View view = null;
        if (target instanceof Activity) {
            Activity activity = (Activity) target;
            view = activity.findViewById(annotation.value());
        } else if (target instanceof Fragment) {
            Fragment fragment = (Fragment) target;
            view = fragment.getView().findViewById(annotation.value());
        } else {
            try {
                Class supportFragment = Class.forName("android.support.v4.app.Fragment");
                if (supportFragment.isAssignableFrom(target.getClass())) {
                    Method getView = supportFragment.getMethod("getView");
                    View viewGroup = (View) getView.invoke(target);
                    view = viewGroup.findViewById(annotation.value());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (view == null) {
            return;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Class[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1) {
                        method.invoke(target, view);
                    } else {
                        method.invoke(target);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
