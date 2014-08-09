package net.sarazan.bismarck.procedures.persisters;

import android.content.Context;

import net.sarazan.bismarck.procedures.i.Persister;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

/**
 * Created by Aaron Sarazan on 6/23/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public abstract class DiskPersister<T> implements Persister<T> {

    private static final String TAG = "DiskPersister";

    private MemoryPersister<T> mCache = new MemoryPersister<>();

    private final String mPath;
    public DiskPersister(String path) {
        mPath = path;
    }

    @Override
    public T get(Context c) {
        T retval = mCache.get(c);
        if (retval != null) return retval;
        File file = new File(mPath);
        try {
            retval = readObject(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCache.update(c, retval);
        return retval;
    }

    @Override
    public void update(Context c, T object) {
        mCache.update(c, object);
        File file = new File(mPath);
        File parent = file.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            throw new RuntimeException("Could not create directory");
        }
        try {
            writeObject(file, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear(Context c) {
        mCache.clear(c);
        File file = new File(mPath);
        file.delete();
    }

    @Nullable
    protected abstract T readObject(File file) throws IOException, ClassNotFoundException;
    protected abstract void writeObject(File file, @NotNull T object) throws IOException;
}
