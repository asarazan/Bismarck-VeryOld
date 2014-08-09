package net.sarazan.bismarck;


import net.sarazan.bismarck.run.Runnable1;

/**
 * Created by Aaron Sarazan on 6/23/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public interface Bismarck<T> {
    T get();
    void fetch(Runnable1<T> callback, boolean force);
    void set(T object);
    void add(T object);
    void delete(T object);
    void invalidate();
}
