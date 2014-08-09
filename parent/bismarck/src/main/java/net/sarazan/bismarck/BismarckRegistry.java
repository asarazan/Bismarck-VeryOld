package net.sarazan.bismarck;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Aaron Sarazan on 6/23/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public final class BismarckRegistry {

    private static final String TAG = "BismarckRegistry";

    private static final ConcurrentHashMap<Class, Bismarck> MAP = new ConcurrentHashMap<>();

    public static <T> void register(Class<T> syncClass, Bismarck<T> bismarck) {
        MAP.put(syncClass, bismarck);
    }

    public static <T> Bismarck<T> get(Class<T> syncClass) {
        return MAP.get(syncClass);
    }
}
