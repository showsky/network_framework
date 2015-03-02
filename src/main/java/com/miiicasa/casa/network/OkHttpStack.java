package com.miiicasa.casa.network;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by showsky on 15/3/2.
 */
public class OkHttpStack extends HurlStack {

    private final static String TAG = OkHttpStack.class.getSimpleName();
    private final OkUrlFactory okUrlFactory;

    public OkHttpStack() {
        this(new OkUrlFactory(Network.getInstance().getOkHttpClient()));
    }

    public OkHttpStack(OkUrlFactory okUrlFactory) {
        if (okUrlFactory == null) {
            throw new NullPointerException("Client must not be null.");
        }
        this.okUrlFactory = okUrlFactory;
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return okUrlFactory.open(url);
    }
}
