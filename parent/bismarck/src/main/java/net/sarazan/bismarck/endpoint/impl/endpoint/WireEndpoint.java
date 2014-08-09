package net.sarazan.bismarck.endpoint.impl.endpoint;

import android.content.Context;

import com.squareup.wire.Message;

import net.sarazan.bismarck.endpoint.impl.serializer.WireSerializer;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by Aaron Sarazan on 5/13/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public abstract class WireEndpoint<T extends Message, R extends Message> extends BaseEndpoint<T, R> {

    private static final String TAG = "WireEndpoint";

    protected WireEndpoint(@NotNull Context context, @NotNull Class<R> cls) {
        super(context);
        setSerializer(new WireSerializer<T, R>(cls));
    }

    @NotNull
    @Override
    protected String getMethod() {
        return "POST";
    }

    @NotNull
    @Override
    protected Map<String, String> applyHeaders(@NotNull Map<String, String> headers) {
        headers = super.applyHeaders(headers);
        headers.put("Content-Type", "application/x-protobuf");
        return headers;
    }
}
