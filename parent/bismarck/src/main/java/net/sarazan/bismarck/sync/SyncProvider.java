package net.sarazan.bismarck.sync;

import android.content.Context;

import net.sarazan.bismarck.run.RunnableR;
import net.sarazan.bismarck.util.IRateLimiter;
import net.sarazan.bismarck.util.RateLimiter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Aaron Sarazan on 6/1/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public abstract class SyncProvider<T> {

    private static final String TAG = "SyncProvider";

    @NotNull
    private String mTag;

    private IRateLimiter mRateLimiter;

    protected SyncProvider(@NotNull String tag) {
        mTag = tag;
        mRateLimiter = new RateLimiter(5 * 60 * 1000L);
    }

    public SyncProvider<T> withRateLimiter(IRateLimiter limiter) {
        mRateLimiter = limiter;
        return this;
    }

    public void fetch(@Nullable Context context, boolean force) {
        if (context == null) return;
        if (mRateLimiter != null) {
            mRateLimiter.run(context, new RunnableR<Boolean>() {
                @Override
                public Boolean run() {
                    return null;
                }
            }, force);
        } else {
            // TODO
        }
    }
}
