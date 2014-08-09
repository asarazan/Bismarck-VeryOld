package net.sarazan.bismarck.procedures.persisters;

import com.squareup.wire.Message;
import com.squareup.wire.Wire;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Aaron Sarazan on 6/23/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public class WireDiskPersister<T extends Message> extends DiskPersister<T> {

    private static final String TAG = "WireDiskPersister";

    private final Class<T> mClass;
    public WireDiskPersister(String path, Class<T> messageClass) {
        super(path);
        mClass = messageClass;
    }

    @Nullable
    @Override
    protected T readObject(File file) throws IOException, ClassNotFoundException {
        InputStream is = new FileInputStream(file);
        return new Wire().parseFrom(is, mClass);
    }

    @Override
    protected void writeObject(File file, @NotNull T object) throws IOException {
        new FileOutputStream(file).write(object.toByteArray());
    }
}
