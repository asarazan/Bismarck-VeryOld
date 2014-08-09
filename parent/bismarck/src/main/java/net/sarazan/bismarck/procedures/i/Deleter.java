package net.sarazan.bismarck.procedures.i;

/**
 * Created by Aaron Sarazan on 6/23/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public interface Deleter<T> {
    void delete(T object);
}
