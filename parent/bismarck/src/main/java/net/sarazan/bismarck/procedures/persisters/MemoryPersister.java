package net.sarazan.bismarck.procedures.persisters;

import android.content.Context;

import net.sarazan.bismarck.procedures.i.Persister;

/**
 * Created by Aaron Sarazan on 6/23/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public class MemoryPersister<T> implements Persister<T> {

    private static final String TAG = "MemoryPersister";

    private T mCached;

    @Override
    public T get(Context c) {
        return mCached;
    }

    @Override
    public void update(Context c, T object) {
        mCached = object;
    }

    @Override
    public void clear(Context c) {
        mCached = null;
    }
}
