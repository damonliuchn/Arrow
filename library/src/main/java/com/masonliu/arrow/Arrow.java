package com.masonliu.arrow;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masonliu.arrow.handler.ContentViewHandler;
import com.masonliu.arrow.handler.InjectExtraHandler;
import com.masonliu.arrow.handler.InjectFieldHandler;
import com.masonliu.arrow.handler.InjectViewHandler;
import com.masonliu.arrow.handler.OnClickHandler;
import com.masonliu.arrow.handler.OnPostInjectHandler;
import com.masonliu.arrow.model.ClassInfo;

/*
1、对象注入
2、单例
3、Provider 避免循环依赖，延迟初始化  两种方案
3、Application注入
7、onClick注入
5、setContentView
7、OnPostInject
7、support包里的Fragment处理
6、Extra注入
8、
 */

/**
 * Created by liumeng on 16/11/30.
 */
public class Arrow {
    private static Application application;

    public static void init(Application mApplication) {
        application = mApplication;
    }

    public static void inject(Object target) {
        if (target instanceof Activity) {
            InjectFieldHandler.inject(target);
            InjectExtraHandler.inject(target);
            ContentViewHandler.inject(target);
            InjectViewHandler.inject(target);
            OnClickHandler.inject(target);
        } else if (target instanceof Fragment || isV4Fragment(target)) {
            InjectFieldHandler.inject(target);
            InjectExtraHandler.inject(target);
        } else {
            InjectFieldHandler.inject(target);
        }
        OnPostInjectHandler.inject(target);
    }

    public static View injectFragmentOnCreateView(Object fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return ContentViewHandler.inject(fragment, inflater, container, savedInstanceState);
    }

    public static void injectFragmentOnViewCreated(Object target) {
        if (target instanceof Fragment || isV4Fragment(target)) {
            InjectViewHandler.inject(target);
            OnClickHandler.inject(target);
        }
    }

    public static Application getApplication() {
        if (application == null) {
            throw new RuntimeException("Arrow not init");
        }
        return application;
    }

    public static <Q> Q getInstance(Class<Q> tClass) {
        return (Q) (InjectFieldHandler.getNoProviderInstance(new ClassInfo(tClass)));
    }

    public static View getContentView(Activity activity) {
        return ((ViewGroup) (activity.findViewById(android.R.id.content))).getChildAt(0);
    }

    private static boolean isV4Fragment(Object target) {
        try {
            Class supportFragment = Class.forName("android.support.v4.app.Fragment");
            if (supportFragment.isAssignableFrom(target.getClass())) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}