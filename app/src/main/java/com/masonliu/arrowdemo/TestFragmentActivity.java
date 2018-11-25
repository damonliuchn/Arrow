package com.masonliu.arrowdemo;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.masonliu.arrow.Arrow;
import com.masonliu.arrow.annotation.ContentView;
import com.masonliu.arrow.annotation.InjectExtra;

import javax.inject.Inject;

@ContentView(R.layout.container)
public class TestFragmentActivity extends AppCompatActivity {
    @Inject
    private A a;

    @Inject
    private C c;

    @Inject
    private Application application;

    @InjectExtra("aaa")
    private String strArgument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Arrow.injectNoView(this);
        Arrow.injectView(this);
        TestFragment fragment = TestFragment.instantiate("testStrArgument", 100, null);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        Toast.makeText(this, "strArgument:" + strArgument, Toast.LENGTH_LONG).show();

    }
}
