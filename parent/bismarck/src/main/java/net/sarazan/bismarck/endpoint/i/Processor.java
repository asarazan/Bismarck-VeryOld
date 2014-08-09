package net.sarazan.bismarck.endpoint.i;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;

/**
 * Created by Aaron Sarazan on 5/18/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public interface Processor {

    void processConnection(@NotNull Context context, @NotNull HttpURLConnection conn);
}
