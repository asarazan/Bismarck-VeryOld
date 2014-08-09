package net.sarazan.bismarck.endpoint.i;

/**
 * Created by Aaron Sarazan on 5/13/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public interface Response<T> {

    boolean isSuccess();
    T getData();
    Exception getException();
    int getCode();
}
