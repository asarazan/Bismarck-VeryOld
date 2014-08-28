package net.sarazan.bismarck;

import android.content.Context;

import net.sarazan.bismarck.procedures.i.Adder;
import net.sarazan.bismarck.procedures.i.Callback;
import net.sarazan.bismarck.procedures.i.Deleter;
import net.sarazan.bismarck.procedures.i.Getter;
import net.sarazan.bismarck.procedures.i.Persister;
import net.sarazan.bismarck.procedures.i.Setter;
import net.sarazan.bismarck.procedures.persisters.MemoryPersister;
import net.sarazan.bismarck.run.Runnable1;
import net.sarazan.bismarck.run.RunnableR;
import net.sarazan.bismarck.rate.RateLimiter;

/**
 * Created by Aaron Sarazan on 6/22/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public class ComponentBismarck<T> implements Bismarck<T> {

    private static final String TAG = "BismarckSync";

    private final Context mContext;

    private final Getter<T> mGetter;

    private final Setter<T> mSetter;

    private final Adder<T> mAdder;

    private final Deleter<T> mDeleter;

    private final Persister<T> mPersister;

    private final RateLimiter mRateLimiter;

    public ComponentBismarck(Context context, Getter<T> getter, Setter<T> setter, Adder<T> adder, Deleter<T> deleter, Persister<T> persister, RateLimiter rateLimiter) {
        mContext = context;
        mGetter = getter;
        mSetter = setter;
        mAdder = adder;
        mDeleter = deleter;
        mPersister = persister;
        mRateLimiter = rateLimiter;
    }

    @Override
    public T get() {
        return mPersister.get(mContext);
    }

    @Override
    public void fetch(final Runnable1<T> callback, boolean force) {
        assertNotNull(mGetter);

        final Callback<T> callbackInternal = new Callback<T>() {
            @Override
            public void onSuccess(T response) {
                mPersister.update(mContext, response);
                callback.run(response);
            }

            @Override
            public void onFailure(Exception e) {
                mRateLimiter.reset(mContext);
            }
        };

        if (mRateLimiter == null) {
            mGetter.get(mContext, callbackInternal);
        } else {
            mRateLimiter.run(mContext, new RunnableR<Boolean>() {
                @Override
                public Boolean run() {
                    mGetter.get(mContext, callbackInternal);
                    return true;
                }
            }, force);
        }
    }

    @Override
    public void set(T object) {
        if (mPersister != null) {
            mPersister.update(mContext, object);
        }
        if (mSetter != null) {
            mSetter.set(mContext, object);
        }
    }

    @Override
    public void add(T object) {
        assertNotNull(mAdder);
        mAdder.add(mContext, object);
    }

    @Override
    public void delete(T object) {
        assertNotNull(mDeleter);
        mDeleter.delete(object);
    }

    @Override
    public void invalidate() {
        assertNotNull(mRateLimiter);
        mRateLimiter.reset(mContext);
    }

    private void assertNotNull(Object obj) {
        if (obj == null) throw new RuntimeException("Null component");
    }

    public static class Builder<T> {

        private final Context mContext;

        private Getter<T> mGetter;

        private Setter<T> mSetter;

        private Adder<T> mAdder;

        private Deleter<T> mDeleter;

        private Persister<T> mPersister;

        private RateLimiter mRateLimiter;

        public Builder(Context context) {
            mContext = context.getApplicationContext() != null ? context.getApplicationContext() : context;
        }

        public ComponentBismarck<T> build() {
            Persister<T> persister = mPersister;
            if (persister == null) {
                persister = new MemoryPersister<>();
            }
            return new ComponentBismarck<>(mContext, mGetter, mSetter, mAdder, mDeleter, persister, mRateLimiter);
        }

        public Builder<T> get(Getter<T> getter) {
            mGetter = getter;
            return this;
        }

        public Builder<T> set(Setter<T> setter) {
            mSetter = setter;
            return this;
        }

        public Builder<T> add(Adder<T> adder) {
            mAdder = adder;
            return this;
        }

        public Builder<T> delete(Deleter<T> deleter) {
            mDeleter = deleter;
            return this;
        }

        public Builder<T> persist(Persister<T> persister) {
            mPersister = persister;
            return this;
        }

        public Builder<T> rateLimiter(RateLimiter rateLimiter) {
            mRateLimiter = rateLimiter;
            return this;
        }
    }

}
