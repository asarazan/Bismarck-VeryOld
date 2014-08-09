package net.sarazan.bismarck.endpoint.impl.response;

import net.sarazan.bismarck.endpoint.i.Response;

/**
 * Created by Aaron Sarazan on 5/13/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public class BasicResponse<T> implements Response<T> {

    private static final String TAG = "BasicResponse";

    private final T mData;
    private final Exception mException;
    private final int mCode;

    public BasicResponse(T data) {
        mData = data;
        mCode = 200;
        mException = null;
    }

    public BasicResponse(Exception ex) {
        mData = null;
        mException = ex;
        mCode = -1;
    }

    @Override
    public boolean isSuccess() {
        return mException == null;
    }

    @Override
    public T getData() {
        return mData;
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
