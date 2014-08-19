package net.sarazan.bismarck.endpoint.impl.endpoint;

import android.content.Context;
import android.os.AsyncTask;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;

import net.sarazan.bismarck.endpoint.factory.Responses;
import net.sarazan.bismarck.endpoint.i.Callback;
import net.sarazan.bismarck.endpoint.i.Endpoint;
import net.sarazan.bismarck.endpoint.i.Processor;
import net.sarazan.bismarck.endpoint.i.Response;
import net.sarazan.bismarck.endpoint.i.Serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aaron Sarazan on 5/18/14
 * Copyright(c) 2014 Manotaur, LLC.
 */
public abstract class BaseEndpoint<REQ, RES> implements Endpoint<REQ, RES> {

    public enum Method {
        GET,
        POST,
        // TODO
    }

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
        Request.Builder b = new Builder().url(getURL());
        Map<String, String> headers = applyHeaders(new HashMap<String, String>());
        for (String key : headers.keySet()) {
            b.header(key, headers.get(key));
        }
        if (mProcessor != null) {
            mProcessor.processRequest(c, b);
        }
        InputStream is = null;
        try {
            switch (method()) {
                case GET:
                    b.get();
                    break;
                case POST:
                    REQ request = getRequest();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    mSerializer.serializeRequest(bos, request);
                    b.post(RequestBody.create(MediaType.parse(contentType()), bos.toByteArray()));
                    break;
            }
            Request req = b.build();
            Call call = client.newCall(req);
            com.squareup.okhttp.Response res = call.execute();
            if (!res.isSuccessful()) {
                return fail(Responses.<RES>failedResponse(res.code()));
            }
            is = res.body().byteStream();
            RES response = mSerializer.deserializeResponse(is);
            return succeed(Responses.createResponse(response));
        } catch (IOException e) {
            fail(Responses.<RES>failedResponse(e));
        } finally {
            try {
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
    protected Method method() {
        return Method.GET;
    }

    @NotNull
    protected String contentType() {
        return "text/plain";
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
