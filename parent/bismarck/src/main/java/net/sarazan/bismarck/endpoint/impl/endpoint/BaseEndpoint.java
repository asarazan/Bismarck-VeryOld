package net.sarazan.bismarck.endpoint.impl.endpoint;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import net.sarazan.bismarck.endpoint.factory.Responses;
import net.sarazan.bismarck.endpoint.i.Callback;
import net.sarazan.bismarck.endpoint.i.Endpoint;
import net.sarazan.bismarck.endpoint.i.Processor;
import net.sarazan.bismarck.endpoint.i.Response;
import net.sarazan.bismarck.endpoint.i.Serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aaron Sarazan on 5/18/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public abstract class BaseEndpoint<REQ, RES> implements Endpoint<REQ, RES> {

    private static final String TAG = "BaseEndpoint";

    private Context mContext;
    private Serializer<REQ, RES> mSerializer;
    private Processor mProcessor;

    public BaseEndpoint(Context context) {
        mContext = context;
    }

    @NotNull
    protected abstract URL getURL();

    @Nullable
    protected abstract REQ getRequest();

    @NotNull
    @Override
    public Response<RES> fetch() {
        Context c = getContext();
        if (c == null) {
            return Responses.failedResponse(new IllegalArgumentException("Null context"));
        }
        OkHttpClient client = new OkHttpClient();
        HttpURLConnection conn = client.open(getURL());
        Map<String, String> headers = applyHeaders(new HashMap<String, String>());
        for (String key : headers.keySet()) {
            conn.addRequestProperty(key, headers.get(key));
        }
        if (mProcessor != null) {
            mProcessor.processConnection(c, conn);
        }
        OutputStream os = null;
        InputStream is = null;
        try {
            conn.setRequestMethod(getMethod());
            os = conn.getOutputStream();
            REQ request = getRequest();
            int length = mSerializer.serializeRequest(os, request);
            Log.d(TAG, "Sending " + length + " bytes");
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return fail(Responses.<RES>failedResponse(conn.getResponseCode()));
            }
            is = conn.getInputStream();
            RES response = mSerializer.deserializeResponse(is);
            return succeed(Responses.createResponse(response));
        } catch (IOException e) {
            fail(Responses.<RES>failedResponse(e));
        } finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (IOException ignored) {}
        }
        return Responses.failedResponse(new Exception("Unknown Error"));
    }

    @Override
    public void fetchAsync(final Callback<RES> callback) {
        new AsyncTask<Void, Void, Response<RES>>() {
            @Override
            protected Response<RES> doInBackground(Void... params) {
                return fetch();
            }

            @Override
            protected void onPostExecute(Response<RES> response) {
                super.onPostExecute(response);
                if (callback != null) {
                    callback.onComplete(response);
                }
            }
        }.execute();
    }

    @NotNull
    protected Map<String, String> applyHeaders(@NotNull Map<String, String> headers) {
        return headers;
    }

    @NotNull
    protected String getMethod() {
        return "GET";
    }

    private Response<RES> succeed(Response<RES> response) {
        onSuccess(response);
        return response;
    }

    private Response<RES> fail(Response<RES> response) {
        onFailure(response);
        return response;
    }

    protected void onSuccess(Response<RES> response) {

    }

    protected void onFailure(Response<RES> response) {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public Serializer<REQ, RES> getSerializer() {
        return mSerializer;
    }

    public void setSerializer(Serializer<REQ, RES> serializer) {
        mSerializer = serializer;
    }

    public Processor getProcessor() {
        return mProcessor;
    }

    public void setProcessor(Processor processor) {
        mProcessor = processor;
    }

    @Override
    public String toString() {
        return getURL().getPath();
    }
}
