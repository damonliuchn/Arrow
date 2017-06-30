package com.masonliu.arrowdemo;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class A {
    final B b;
    int aa;
    @Inject
    public A(B b) {
        this.b = b;
    }

    public String print() {
        return "I am A"+(aa++);
    }
}
