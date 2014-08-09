package net.sarazan.bismarck.procedures.getters;

import android.content.Context;

import net.sarazan.bismarck.endpoint.i.Endpoint;
import net.sarazan.bismarck.endpoint.i.Response;
import net.sarazan.bismarck.procedures.i.Getter;
import net.sarazan.bismarck.run.Runnable1;
import net.sarazan.bismarck.sync.Sync;

/**
 * Created by Aaron Sarazan on 6/22/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public abstract class EndpointGetter<T> implements Getter<T> {

    private static final String TAG = "EndpointGetter";

    @Override
    public void get(final Context c, final Runnable1<Response<T>> callback) {
        Endpoint<?, T> endpoint = createEndpoint(c);
        Sync.async(endpoint.toString(), new Runnable() {
            @Override
            public void run() {
                Response<T> response = createEndpoint(c).fetch();
                if (callback != null) {
                    callback.run(response);
                }
            }
        }, Sync.Priority.NORMAL);
    }

    protected abstract Endpoint<?, T> createEndpoint(Context c);
}
