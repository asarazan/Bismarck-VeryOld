package net.sarazan.bismarck.rate;

import android.content.Context;

import net.sarazan.bismarck.run.RunnableR;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Aaron Sarazan on 6/1/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public class RateLimiter implements IRateLimiter {

    private static final String TAG = "RateLimiter";

    private final long mInterval;

    private long mLast;

    public RateLimiter(long interval) {
        mInterval = interval;
    }

    @Override
    public boolean run(@NotNull Context context, RunnableR<Boolean> runnable, boolean force) {
        long time = System.currentTimeMillis();
        if (force) return true;
        if (time > mLast + mInterval && runnable.run()) {
            mLast = time;
            return true;
        }
        return false;
    }

    @Override
    public void reset(@NotNull Context context) {
        mLast = 0L;
    }
}
