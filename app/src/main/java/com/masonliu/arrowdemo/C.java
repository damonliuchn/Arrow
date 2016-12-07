package com.masonliu.arrowdemo;

import android.app.Application;
import android.util.Log;

import com.masonliu.arrow.annotation.OnPostInject;

import javax.inject.Inject;

public class C {
    @Inject
    private Application application;

    public C() {
        Log.i("Arrow", "This is C's constructor");
    }

    @OnPostInject
    public void onPostInject() {
        Log.i("Arrow", "This is C's onPostInject mehtod");
    }

    public String foo() {
        return "I am C" + application.toString();
    }
}
