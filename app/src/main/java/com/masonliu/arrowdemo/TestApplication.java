package com.masonliu.arrowdemo;

import android.app.Application;

import com.masonliu.arrow.Arrow;


public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Arrow.init(this);
    }
}
