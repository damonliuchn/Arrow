package com.masonliu.arrowdemo;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class B {
    public String title = " imb ";
    Provider<A> a;

    @Inject
    public B(Provider<A> a) {
        this.a = a;
    }

}
