package net.sarazan.bismarck.endpoint.i;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Aaron Sarazan on 5/18/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public interface Serializer<REQ, RES> {

    @NotNull
    RES deserializeResponse(InputStream is) throws IOException;

    int serializeRequest(@NotNull OutputStream os, REQ request) throws IOException;
}
