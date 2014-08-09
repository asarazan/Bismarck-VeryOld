package net.sarazan.bismarck.endpoint.factory;

import net.sarazan.bismarck.endpoint.i.Response;
import net.sarazan.bismarck.endpoint.impl.response.BasicResponse;
import net.sarazan.bismarck.endpoint.impl.response.FailedResponse;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Aaron Sarazan on 5/13/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public final class Responses {

    private static final String TAG = "Responses";

    private Responses() {}

    public static <T> Response<T> failedResponse(Exception cause) {
        return new FailedResponse<>(cause);
    }

    public static <T> Response<T> failedResponse(int code) {
        return new FailedResponse<>(code);
    }

    public static <T> Response<T> failedResponse(Response<?> other) {
        return new FailedResponse<>(other);
    }

    public static <T> Response<T> createResponse(@NotNull T data) {
        return new BasicResponse<>(data);
    }
}
