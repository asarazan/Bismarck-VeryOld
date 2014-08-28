package net.sarazan.bismarck.procedures.i;

/**
 * Created by Aaron Sarazan on 8/28/14
 * Copyright(c) 2014 Level, Inc.
 */
public interface Callback<T> {

    void onSuccess(T response);
    void onFailure(Exception e);
}
