package com.masonliu.arrowdemo;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class B {
    Provider<A> a;

    @Inject
    public B(Provider<A> a) {
        this.a = a;
    }

}
