package net.sarazan.bismarck.sync;

import android.os.AsyncTask;

import net.sarazan.bismarck.run.RunnableR;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aaron Sarazan on 6/1/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public class Sync {

    private static final String TAG = "Sync";

    private Sync() {}

    public enum Priority {
        HIGH,
        NORMAL,
    }

    private static final Executor ASYNC_EXEC = new ThreadPoolExecutor(5, 20, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    public static void async(final String tag, @NotNull final Runnable runnable, Priority priority) {
        final Runnable finalRunnable = new Runnable() {
            @Override
            public void run() {
                sync(tag, new RunnableR<Void>() {
                    @Override
                    public Void run() {
                        runnable.run();
                        return null;
                    }
                });

            }
        };
        if (priority == Priority.HIGH) {
            new Thread(finalRunnable).start();
        } else {
            try {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        finalRunnable.run();
                        return null;
                    }
                }.executeOnExecutor(ASYNC_EXEC);
            } catch (RejectedExecutionException ignored) {}
        }
    }

    @Nullable
    public static <T> T sync(String tag, @NotNull final RunnableR<T> runnable) {
        return runnable.run();
    }
}
