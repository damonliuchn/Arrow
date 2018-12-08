package com.masonliu.arrow.handler;

import android.util.Log;

import com.masonliu.arrow.Arrow;
import com.masonliu.arrow.annotation.InjectExtra;
import com.masonliu.arrow.annotation.InjectView;
import com.masonliu.arrow.model.ClassInfo;
import com.masonliu.arrow.model.FieldInfo;
import com.masonliu.arrow.provider.DefaultProvidersModule;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Created by liumeng on 16/11/30.
 */

public class InjectFieldHandler {
    static final Map<ClassInfo, Provider> providers = new ConcurrentHashMap<>();
    static final Map<ClassInfo, Object> singletons = new ConcurrentHashMap<>();
    static final Map<Class, List<FieldInfo>> fieldInfosMap = new ConcurrentHashMap<>(0);

    public static void inject(Object target) {
        //target.getClass() 是单例，fieldInfosMap.containsKey 判断的是hashcode
        if (!fieldInfosMap.containsKey(target.getClass())) {
            fieldInfosMap.put(target.getClass(), getFieldInfos(target.getClass()));
        }
        for (FieldInfo fieldInfo : fieldInfosMap.get(target.getClass())) {
            Field field = fieldInfo.getField();
            if (field.isAnnotationPresent(Inject.class)) {
                try {
                    field.set(target, fieldInfo.isProviderType() ? provider(fieldInfo.getClassInfo(), null) : getNoProviderInstance(fieldInfo.getClassInfo()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object getNoProviderInstance(ClassInfo classInfo) {
        return provider(classInfo, null).get();
    }

    static List<FieldInfo> getFieldInfos(Class clazz) {
        Class current = clazz;
        List<FieldInfo> fieldInfos = new ArrayList<>();
        while (!current.equals(Object.class)) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)
                        || field.isAnnotationPresent(InjectExtra.class)
                        || field.isAnnotationPresent(InjectView.class)) {
                    field.setAccessible(true);
                    fieldInfos.add(new FieldInfo(field));
                }
            }
            current = current.getSuperclass();
        }
        return fieldInfos;
    }

    /**
     * @param classInfo
     * @param chain     check circular dependency when providers init
     * @return
     */
    private static Provider provider(final ClassInfo classInfo, Set<ClassInfo> chain) {
        if (!providers.containsKey(classInfo)) {
            Provider provider = null;
            //get Provider from module
            if (DefaultProvidersModule.getMethod(classInfo) != null) {
                try {
                    provider = (Provider) DefaultProvidersModule.getMethod(classInfo).invoke(null);
                    Log.e("ddddd", "ddd");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
            //get Provider from Constructor
            {
                final Provider[] paramProviders = paramProvidersInClassConstructor(classInfo, chain);
                final Provider normalProvider = new Provider() {
                    @Override
                    public Object get() {
                        try {
                            Object object = classInfo.getConstructor().newInstance(getParamsInstance(paramProviders));
                            Arrow.injectNoView(object);
                            return object;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };

                if (classInfo.getClazz().getAnnotation(Singleton.class) != null) {
                    provider = new Provider() {
                        @Override
                        public Object get() {
                            if (!singletons.containsKey(classInfo)) {
                                synchronized (singletons) {
                                    if (!singletons.containsKey(classInfo)) {
                                        singletons.put(classInfo, normalProvider.get());
                                    }
                                }
                            }
                            return singletons.get(classInfo);
                        }
                    };
                } else {
                    provider = normalProvider;
                }
            }
            providers.put(classInfo, provider);
        }
        return providers.get(classInfo);
    }


    private static Provider[] paramProvidersInClassConstructor(final ClassInfo classInfo, final Set<ClassInfo> chain) {
        Class[] parameterClasses = classInfo.getConstructor().getParameterTypes();
        Type[] parameterTypes = classInfo.getConstructor().getGenericParameterTypes();//返回Provider<T> T的类型
        Provider[] providers = new Provider[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; ++i) {
            Class parameterClass = parameterClasses[i];

            Class providerType = Provider.class.equals(parameterClass) ?
                    (Class<?>) ((ParameterizedType) parameterTypes[i]).getActualTypeArguments()[0] :
                    null;

            if (providerType == null) {
                final ClassInfo paramClassInfo = new ClassInfo(parameterClass);
                final Set<ClassInfo> currentChain = appendChain(chain, classInfo);
                //currentChain.contains 判断的是equal，所以要重写ClassInfo的equal
                if (currentChain.contains(paramClassInfo)) {
                    throw new RuntimeException("Arrow Circular dependency:" + printChain(currentChain, paramClassInfo));
                }
                providers[i] = new Provider() {
                    @Override
                    public Object get() {
                        return provider(paramClassInfo, currentChain).get();
                    }
                };
            } else {
                final ClassInfo providerClassInfo = new ClassInfo(providerType);
                providers[i] = new Provider() {
                    @Override
                    public Object get() {
                        return provider(providerClassInfo, null);
                    }
                };
            }
        }
        return providers;
    }

    private static Set<ClassInfo> appendChain(Set<ClassInfo> set, ClassInfo classInfo
    ) {
        if (set != null && !set.isEmpty()) {
            Set<ClassInfo> appended = new LinkedHashSet<>(set);
            appended.add(classInfo);
            return appended;
        } else {
            return Collections.singleton(classInfo);
        }
    }

    private static String printChain(Set<ClassInfo> chain, ClassInfo lastFieldInfo) {
        StringBuilder chainString = new StringBuilder();
        for (ClassInfo fieldInfo : chain) {
            chainString.append(fieldInfo.toString()).append(" -> ");
        }
        return chainString.append(lastFieldInfo.toString()).toString();
    }

    private static Object[] getParamsInstance(Provider[] paramProviders) {
        Object[] params = new Object[paramProviders.length];
        for (int i = 0; i < paramProviders.length; ++i) {
            params[i] = paramProviders[i].get();
        }
        return params;
    }
}
