package net.sarazan.bismarck.endpoint.i;

import android.content.Context;

import com.squareup.okhttp.Request.Builder;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Aaron Sarazan on 5/18/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public interface Processor {

    void processRequest(@NotNull Context context, @NotNull Builder requestBuilder);
}
