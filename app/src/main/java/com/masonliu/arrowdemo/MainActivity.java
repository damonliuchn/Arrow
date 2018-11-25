package com.masonliu.arrowdemo;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.masonliu.arrow.Arrow;
import com.masonliu.arrow.annotation.ContentView;
import com.masonliu.arrow.annotation.InjectView;
import com.masonliu.arrow.annotation.OnClick;

import javax.inject.Inject;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.text)
    TextView textView;
    @Inject
    private A a;
    @Inject
    private C c;
    @Inject
    private Application application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Arrow.inject(this);
        //TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(a.b.a.get().print() + "ssss");
    }

    @OnClick(R.id.button)
    public void onClickTest() {
        //Toast.makeText(this, c.foo(), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, application.toString(), Toast.LENGTH_LONG).show();
        Toast.makeText(this, Arrow.getInstance(A.class).print(), 1).show();
        Toast.makeText(this, Arrow.getInstance(C.class).foo(), 1).show();

    }

    @OnClick(R.id.go_fragment)
    public void onGoFragment() {
        Intent intent = new Intent(this, TestFragmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("aaa", "bbbb");
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
