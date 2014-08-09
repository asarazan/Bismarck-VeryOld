package net.sarazan.bismarck.procedures.i;

import android.content.Context;

import net.sarazan.bismarck.endpoint.i.Response;
import net.sarazan.bismarck.run.Runnable1;

/**
 * Created by Aaron Sarazan on 6/22/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public interface Getter<T> {
    void get(Context c, Runnable1<Response<T>> callback);
}
