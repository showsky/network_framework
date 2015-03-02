package com.miiicasa.casa_lib.network;

import com.miiicasa.Config;
import com.miiicasa.casa_lib.BuildConfig;
import com.miiicasa.casa_lib.exception.NetworkException;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by showsky on 15/3/2.
 */
public class Network {

    private final static String TAG = Network.class.getSimpleName();
    private final static MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private final static String ACCEPT_LANGUAGE = "Accept-Language";
    private String userAgent = Config.NETWORK_DEFAULT_USER_AGENT;
    private String acceptLanguage = null;
    private OkHttpClient okHttpClient;

    private static class LazyHolder {

        private static final Network INSTANCE = new Network();
    }

    private Network() {
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(
            Config.NETWORK_CONNECT_TIMEOUT_SECOND,
            TimeUnit.SECONDS
        );
        okHttpClient.setFollowRedirects(true);
        if (Config.PROXY) {
            okHttpClient.setProxy(
                new Proxy(
                    Proxy.Type.HTTP,
                    new InetSocketAddress(Config.PROXY_IP, Config.PROXY_PORT)
                )
            );
        }
    }

    public Network setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Network setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
        return this;
    }

    public static Network getInstance() {
        return LazyHolder.INSTANCE;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    private Request.Builder getRequestBuilder(String url) {
        Request.Builder builder = new Request.Builder();
        builder.addHeader(HTTP.USER_AGENT, userAgent);
        if (acceptLanguage != null) {
            builder.addHeader(ACCEPT_LANGUAGE, acceptLanguage);
        }
        builder.url(url);
        return builder;
    }

    private String verify(Request request) throws NetworkException {
        String result = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            if ( ! response.isSuccessful()) {
                throw new NetworkException(NetworkException.TYPE.SERVER_ERROR);
            }
            result = response.body().string();
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            throw new NetworkException(NetworkException.TYPE.NETWORK_ERROR);
        }
        return result;
    }

    public String post(String url, List<NameValuePair> values) throws NetworkException {
        Request.Builder builder = getRequestBuilder(url);
        FormEncodingBuilder formBody = new FormEncodingBuilder();
        if (values != null) {
            for (NameValuePair value : values) {
                formBody.add(value.getName(), value.getValue());
            }
        }
        builder.post(formBody.build());
        return verify(builder.build());
    }

    public String get(String url, List<NameValuePair> values) throws NetworkException {
        String urlPath = (values == null) ? url : url + "?" + URLEncodedUtils.format(values, HTTP.UTF_8);
        Request.Builder builder = getRequestBuilder(urlPath);
        return verify(builder.build());
    }

    public String postFile(String url, List<NameValuePair> values, File file) throws NetworkException {
        Request.Builder builder = getRequestBuilder(url);
        MultipartBuilder multipart = new MultipartBuilder();
        multipart.type(MultipartBuilder.FORM);
        for (NameValuePair value : values) {
            multipart.addFormDataPart(value.getName(), value.getValue());
        }
        multipart.addPart(RequestBody.create(MEDIA_TYPE_JPG, file));
        builder.post(multipart.build());
        return verify(builder.build());
    }
}
