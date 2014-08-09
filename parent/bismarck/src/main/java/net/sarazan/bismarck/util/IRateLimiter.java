package net.sarazan.bismarck.util;

import android.content.Context;

import net.sarazan.bismarck.run.RunnableR;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Aaron Sarazan on 6/1/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public interface IRateLimiter {
    boolean run(@NotNull Context context, RunnableR<Boolean> runnable, boolean force);
    void reset(@NotNull Context context);
}
