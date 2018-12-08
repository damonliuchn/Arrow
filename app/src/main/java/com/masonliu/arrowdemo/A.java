package com.masonliu.arrowdemo;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class A {
    final B b;
    int aa;
    public String title = " ima ";

    /**
     * 在注入新建对象时，优先使用inject标记的 否则使用 无参数构造方法，否则使用第一个构造方法
     */
    public A(B b) {
        this.b = b;
    }

    public String print() {
        return "I am A" + b.title + b.a.get().title;
    }
}
