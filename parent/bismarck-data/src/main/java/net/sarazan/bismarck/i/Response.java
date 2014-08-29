package net.sarazan.bismarck.i;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Aaron Sarazan on 10/13/13
 * Copyright(c) 2013 Level, Inc.
 */
public interface Response<T> {

    public T getData();

    @Nullable
    public Exception getException();

    public boolean isSuccess();

    public int getStatus();
}
