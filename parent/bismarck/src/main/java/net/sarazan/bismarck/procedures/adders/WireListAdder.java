package net.sarazan.bismarck.procedures.adders;

import android.content.Context;

import com.squareup.wire.Message;

import net.sarazan.bismarck.Bismarck;
import net.sarazan.bismarck.BismarckRegistry;
import net.sarazan.bismarck.procedures.i.Adder;
import net.sarazan.wire.ReflectiveWire;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron Sarazan on 6/22/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public class WireListAdder<T extends Message, R extends Message> implements Adder<T> {

    private static final String TAG = "WireListAdder";

    private final Class<R> mResponseClass;

    private final int mTag;

    public WireListAdder(int tag, Class<R> responseClass) {
        mTag = tag;
        mResponseClass = responseClass;
    }

    @Override
    public void add(Context c, T object) {
        Bismarck<R> other = BismarckRegistry.get(mResponseClass);
        R latest = other.get();
        if (latest == null) {
            throw new RuntimeException("Not prepared");
        }
        Field f = ReflectiveWire.getFieldByTag(latest, mTag);
        List<T> list;
        try {
            list = (List<T>) f.get(latest);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(object);
            Message.Builder<R> b = ReflectiveWire.cloneToBuilder(latest);
            Method builderSetter = b.getClass().getDeclaredMethod(f.getName());
            builderSetter.invoke(b, list);
            latest = b.build();
            other.set(latest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
