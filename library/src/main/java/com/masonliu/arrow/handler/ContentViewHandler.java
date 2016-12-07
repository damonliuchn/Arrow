package com.masonliu.arrow.handler;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masonliu.arrow.annotation.ContentView;

/**
 * Created by liumeng on 16/11/30.
 */

public class ContentViewHandler {
    public static void inject(Object target) {
        if (target instanceof Activity) {
            Activity activity = (Activity) target;
            ContentView annotation = target.getClass().getAnnotation(ContentView.class);
            if (annotation != null) {
                activity.setContentView(annotation.value());
            }
        }
    }

    public static View inject(Object fragment,
                              LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        final ContentView annotation = fragment.getClass().getAnnotation(ContentView.class);
        if (annotation != null) {
            return inflater.inflate(annotation.value(), container, false);
        }
        return null;
    }
}
