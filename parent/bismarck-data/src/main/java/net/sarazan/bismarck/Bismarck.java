package net.sarazan.bismarck;

import android.util.Log;

import com.levelmoney.bismarck.i.Cache;
import com.levelmoney.bismarck.i.Delete;
import com.levelmoney.bismarck.i.Fetch;
import com.levelmoney.bismarck.i.Insert;
import com.levelmoney.common.data.endpoint.Response;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Aaron Sarazan on 8/28/14
 * Copyright(c) 2014 Level, Inc.
 */
public final class Bismarck {

    public static final String TAG = "Bismarck";

    public static class OperationException extends RuntimeException {
        public OperationException() {
            super("Operation not supported");
        }
    }

    public static class BismarckException extends RuntimeException {
        public BismarckException(Throwable throwable) {
            super(throwable);
        }
    }

    private static class Invocation {
        public final Object target;
        public final Method method;
        private Invocation(Object target, Method method) {
            this.target = target;
            this.method = method;
        }
    }

    private final Map<Class, Invocation> mFetch = new ConcurrentHashMap<>();
    private final Map<Class, Invocation> mCache = new ConcurrentHashMap<>();
    private final Map<Class, Invocation> mInsert = new ConcurrentHashMap<>();
    private final Map<Class, Invocation> mDelete = new ConcurrentHashMap<>();

    public <T> void register(T object) {
        Class<?> cls = object.getClass();
        for (Method m : cls.getDeclaredMethods()) {
            for (Annotation a : m.getDeclaredAnnotations()) {
                Log.i(TAG, "Registering annotation " + a + " for class " + cls);
                final Invocation value = new Invocation(object, m);
                if (a instanceof Fetch) {
                    mFetch.put(((Fetch) a).value(), value);
                } else if (a instanceof Cache) {
                    mCache.put(m.getReturnType(), value);
                } else if (a instanceof Insert) {
                    mInsert.put(m.getParameterTypes()[0], value);
                } else if (a instanceof Delete) {
                    mDelete.put(m.getParameterTypes()[0], value);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T cached(Class<T> cls) {
        Invocation i = mCache.get(cls);
        if (i == null) {
            throw new OperationException();
        }
        try {
            return (T) i.method.invoke(i.target);
        } catch (Exception e) {
            throw new BismarckException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Response<T> fetch(Class<T> cls) {
        Invocation i = mFetch.get(cls);
        if (i == null) {
            throw new OperationException();
        }
        try {
            return (Response<T>) i.method.invoke(i.target);
        } catch (Exception e) {
            throw new BismarckException(e);
        }
    }

    public <T> void insert(@NotNull T data) {
        Invocation i = mInsert.get(data.getClass());
        if (i == null) {
            throw new OperationException();
        }
        try {
            i.method.invoke(i.target, data);
        } catch (Exception e) {
            throw new BismarckException(e);
        }
    }

    public <T> void delete(@NotNull T data) {
        Invocation i = mDelete.get(data.getClass());
        if (i == null) {
            throw new OperationException();
        }
        try {
            i.method.invoke(i.target, data);
        } catch (Exception e) {
            throw new BismarckException(e);
        }
    }

    public <T> void delete(Class<T> cls) {
        Invocation i = mDelete.get(cls);
        if (i == null) {
            throw new OperationException();
        }
        try {
            i.method.invoke(i.target, new Object[]{null});
        } catch (Exception e) {
            throw new BismarckException(e);
        }
    }
}
