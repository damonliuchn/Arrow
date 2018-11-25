Arrow
========
Arrow is a lightweight dependency injection library for Android.  

Why
--------
Roboguice is no longer maintained, Dagger2 is a bit complicated, need to write component interface (so I wrote an automatic generation of component interface [Dagger2Plus](https://github.com/MasonLiuChn/Dagger2Plus)


Feature
--------
1.@Inject:

inject object

2.@Singleton:

inject singleton object

3.Provider Interface:

avoid circular dependency
for example：
```java
public class A {
    final B b;

    @Inject
    public A(B b) {
        this.b = b;
    }

    public String print() {
        return "I am A";
    }
}

public class B {
    Provider<A> a;

    @Inject
    public B(Provider<A> a) {
        this.a = a;
    }

}
```

4.Application:

inject application，support to inject manager for future

5.@OnClick:

View setOnClickListener

6.@ContentView:

Activity or Fragment setContentView

7.@OnPostInject:

call this method after Inject action

8.@InjectExtra:

inject Bundle into Activity or Fragment

9.getInstance:

```
Arrow.getInstance(Class clazz)
```
10.@InjectView

Arrow+DataBinding
--------
use DataBinding instead of butterknife
use Arrow to inject object

中文
------
Arrow 是一个轻量级的Android DI 库，没有额外的引用。


为什么
--------

最流行的Android DI框架中，Roboguice已经不再维护了，Dagger2使用有点复杂，需要写component interface（所以我写了一个自动生成component interface的库[Dagger2Plus](https://github.com/MasonLiuChn/Dagger2Plus))，而且Dagger2使用代码生成，对于有代码洁癖的人来说不太喜欢。Arrow使用反射，在目前动不动就8核10核2G3G的硬件环境下，反射带来的性能缺陷不再明显，作为个人来说还是能接受的。

特性
--------
1、@Inject

对象注入

2、@Singleton

单例对象注入

3、Provider 接口

处理循环依赖
例如：
```java
public class A {
    final B b;

    @Inject
    public A(B b) {
        this.b = b;
    }

    public String print() {
        return "I am A";
    }
}

public class B {
    Provider<A> a;

    @Inject
    public B(Provider<A> a) {
        this.a = a;
    }

}
```

4、Application注入

后续加入各种Service Manager的注入

5、@OnClick

View点击事件处理

6、@ContentView

支持Activity和Fragment setContentView

7、@OnPostInject

Inject 结束后触发该函数

8、@InjectExtra

支持Activity和Fragment 注入Bundle

9、getInstance

直接获取一个实例，可以是单例
```
Arrow.getInstance(Class clazz)
```
10、@InjectView

Arrow+DataBinding
--------
使用DataBinding替代butterknife
Object的注入使用Arrow

Usage
--------

```groovy
repositories {
    maven {
        maven { url "https://github.com/MasonLiuChn/MasonMavenRepository/raw/maven/releases" }
    }
}
dependencies {
	compile 'com.masonliu:arrow:1.1.5'
}
```

```java
Arrow.init(Application application);
Arrow.inject(Object object);
Arrow.injectFragmentOnCreateView(Object fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
Arrow.injectFragmentOnViewCreated(Object target)
```


#Contact me:

- Blog:http://www.masonliu.com

- Email:MasonLiuChn@gmail.com

License
=======

    Copyright 2016 MasonLiu, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
