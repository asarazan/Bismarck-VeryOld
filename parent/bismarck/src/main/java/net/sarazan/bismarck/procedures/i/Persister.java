package net.sarazan.bismarck.procedures.i;

import android.content.Context;

/**
 * Created by Aaron Sarazan on 6/22/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public interface Persister<T> {
    T get(Context c);
    void update(Context c, T object);
    void clear(Context c);
}
