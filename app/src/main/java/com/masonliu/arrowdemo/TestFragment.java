package com.masonliu.arrowdemo;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.masonliu.arrow.Arrow;
import com.masonliu.arrow.annotation.ContentView;
import com.masonliu.arrow.annotation.InjectExtra;
import com.masonliu.arrow.annotation.OnClick;

import javax.inject.Inject;

/**
 * Created by liumeng on 16/12/5.
 */
@ContentView(R.layout.fragment_test)
public class TestFragment extends Fragment {

    @Inject
    private A a;
    @Inject
    private C c;
    @Inject
    private Application application;

    @InjectExtra("strArgument")
    private String strArgument;

    @InjectExtra("intArgument")
    private int intArgument;

    public static TestFragment instantiate(String strArgument, int intArgument, Bundle data) {
        TestFragment f = null;
        try {
            f = TestFragment.class.newInstance();
            Bundle bundle;
            if (data != null) {
                bundle = data;
            } else {
                bundle = new Bundle();
            }
            bundle.putString("strArgument", strArgument);
            bundle.putInt("intArgument", intArgument);
            f.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return Arrow.injectFragmentOnCreateView(this, inflater, container, savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Arrow.inject(this);
    }


    @OnClick(R.id.button)
    public void onClickTest() {
        Toast.makeText(getContext(), c.foo(), Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), application.toString(), Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "strArgument:" + strArgument, Toast.LENGTH_LONG).show();
    }
}
