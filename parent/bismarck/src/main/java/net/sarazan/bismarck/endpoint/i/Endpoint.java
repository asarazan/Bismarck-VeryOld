package net.sarazan.bismarck.endpoint.i;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Aaron Sarazan on 5/13/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public interface Endpoint<T, R> {

    Context getContext();

    @NotNull Response<R> fetch();
    void fetchAsync(Callback<R> callback);

}
