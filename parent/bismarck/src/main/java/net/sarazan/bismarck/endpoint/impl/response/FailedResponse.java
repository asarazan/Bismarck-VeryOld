package net.sarazan.bismarck.endpoint.impl.response;

import net.sarazan.bismarck.endpoint.i.Response;

/**
 * Created by Aaron Sarazan on 5/13/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public class FailedResponse<T> implements Response<T> {

    private static final String TAG = "FailedResponse";

    private final Exception mException;
    private final int mCode;

    public FailedResponse(Exception exception) {
        mException = exception;
        mCode = -1;
    }

    public FailedResponse(Response<?> other) {
        mException = other.getException();
        mCode = other.getCode();
    }

    public FailedResponse(int code) {
        mException = null;
        mCode = code;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T getData() {
        return null;
    }

    @Override
    public Exception getException() {
        return mException;
    }

    @Override
    public int getCode() {
        return mCode;
    }
}
