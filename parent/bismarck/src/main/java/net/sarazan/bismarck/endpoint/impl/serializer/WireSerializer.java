package net.sarazan.bismarck.endpoint.impl.serializer;

import com.squareup.wire.Message;
import com.squareup.wire.Wire;

import net.sarazan.bismarck.endpoint.i.Serializer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Aaron Sarazan on 5/18/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public class WireSerializer<REQ extends Message, RES extends Message> implements Serializer<REQ, RES> {

    private static final String TAG = "WireSerializer";

    private Class<RES> mCls;
    public WireSerializer(Class<RES> responseClass) {
        mCls = responseClass;
    }

    @NotNull
    @Override
    public RES deserializeResponse(InputStream is) throws IOException {
        return new Wire().parseFrom(is, mCls);
    }

    @Override
    public int serializeRequest(@NotNull OutputStream os, REQ request) throws IOException {
        byte[] bytes = request.toByteArray();
        os.write(bytes);
        return bytes.length;
    }
}
