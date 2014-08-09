package net.sarazan.bismarck.procedures.persisters;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Aaron Sarazan on 6/24/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public class SerializableDiskPersister<T extends Serializable> extends DiskPersister<T> {

    private static final String TAG = "SerializableDiskPersister";

    public SerializableDiskPersister(String path) {
        super(path);
    }

    @Nullable
    @Override
    protected T readObject(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        return (T) ois.readObject();
    }

    @Override
    protected void writeObject(File file, @NotNull T object) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(object);
    }
}
