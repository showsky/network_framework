package com.miiicasa.casa.network;

import com.miiicasa.Config;
import com.miiicasa.casa.exception.NetworkException;
import com.miiicasa.casa.utils.Logger;
import com.miiicasa.casa.utils.NetworkUtils;
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by showsky on 15/3/2.
 */
public class Network {

    private final static String TAG = Network.class.getSimpleName();
    private final static String POST_FILENAME = "file";
    private final static MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
    private final static String ACCEPT_LANGUAGE = "Accept-Language";
    private String userAgent = Config.NETWORK_DEFAULT_USER_AGENT;
    private CookieManager cookieManager = null;
    private String acceptLanguage = null;
    private OkHttpClient okHttpClient = null;

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
        okHttpClient.setCookieHandler(new CookieHandler() {
            @Override
            public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
                return null;
            }

            @Override
            public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {

            }
        });
        if (Config.USE_PERSISTENT_COOKIE) {
            cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            okHttpClient.setCookieHandler(cookieManager);
        }
        if (Config.USE_SSL) {
            try {
                okHttpClient.setSslSocketFactory(NetworkUtils.getSSLSocketFactory());
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel(String TAG) {
        okHttpClient.cancel(TAG);
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

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    public void initSsl(InputStream inputStream) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca = cf.generateCertificate(inputStream);
        inputStream.close();

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        okHttpClient.setSslSocketFactory(context.getSocketFactory());
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
                throw new NetworkException(NetworkException.TYPE.SERVER_ERROR, response.code());
            }
            result = response.body().string();
        } catch (IOException e) {
            if (Logger.isDebug()) {
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
            Logger.d(TAG, "Post url: %s param: %s", url, values.toString());
            for (NameValuePair value : values) {
                formBody.add(value.getName(), value.getValue());
            }
        } else {
            Logger.d(TAG, "Post url: %s", url);
        }
        builder.post(formBody.build());
        return verify(builder.build());
    }

    public String post(String url, Map<String, String> values) throws NetworkException {
        return post(url, values, null);
    }

    public String post(String url, Map<String, String> values, String id) throws NetworkException {
        Request.Builder builder = getRequestBuilder(url);
        FormEncodingBuilder formBody = new FormEncodingBuilder();
        if (values != null) {
            Logger.d(TAG, "Post url: %s param: %s", url, values.toString());
            for (Map.Entry<String, String> entry : values.entrySet()) {
                formBody.add(entry.getKey(), entry.getValue());
            }
        } else {
            Logger.d(TAG, "Post url: %s", url);
        }
        builder.post(formBody.build());
        if (id != null) {
            builder.tag(id);
        }
        return verify(builder.build());
    }

    public String get(String url, List<NameValuePair> values) throws NetworkException {
        String urlPath = (values == null) ? url : url + "?" + URLEncodedUtils.format(values, HTTP.UTF_8);
        Logger.d(TAG, "Get url: %s", urlPath);
        Request.Builder builder = getRequestBuilder(urlPath);
        return verify(builder.build());
    }

    public String get(String url, Map<String, String> values, String id) throws NetworkException {
        String urlPath = (values == null || values.size() == 0) ? url : url + "?" + queryEncode(values);
        Logger.d(TAG, "Get url: %s", urlPath);
        Request.Builder builder = getRequestBuilder(urlPath);
        if (id != null) {
            builder.tag(id);
        }
        return verify(builder.build());
    }

    public String get(String url, Map<String, String> values) throws NetworkException {
        return get(url, values, null);
    }

    public String get(String url, String id) throws NetworkException {
        return get(url, new HashMap<String, String>(), id);
    }

    public String get(String url) throws NetworkException {
        return get(url, new HashMap<String, String>(), null);
    }

    public String postFile(String url, List<NameValuePair> values, File file, String id) throws NetworkException {
        Request.Builder builder = getRequestBuilder(url);
        MultipartBuilder multipart = new MultipartBuilder();
        multipart.type(MultipartBuilder.FORM);
        if (values != null) {
            Logger.d(TAG, "Post file url: %s param: %s", url, values.toString());
            for (NameValuePair value : values) {
                multipart.addFormDataPart(value.getName(), value.getValue());
            }
        } else {
            Logger.d(TAG, "Post file url: %s", url);
        }
        multipart.addFormDataPart(POST_FILENAME, POST_FILENAME, RequestBody.create(MEDIA_TYPE_JPG, file));
        builder.post(multipart.build());
        if (id != null) {
            builder.tag(id);
        }
        return verify(builder.build());
    }

    public String postFile(String url, Map<String, String> values, File file, MediaType mediaType) throws NetworkException {
        Request.Builder builder = getRequestBuilder(url);
        MultipartBuilder multipart = new MultipartBuilder();
        multipart.type(MultipartBuilder.FORM);
        if (values != null) {
            Logger.d(TAG, "Post file url: %s param: %s", url, values.toString());
            for (Map.Entry<String, String> entry : values.entrySet()) {
                multipart.addFormDataPart(entry.getKey(), entry.getValue());
            }
        } else {
            Logger.d(TAG, "Post file url: %s", url);
        }
        if (mediaType == null) {
            multipart.addFormDataPart(POST_FILENAME, file.getName(), RequestBody.create(MEDIA_TYPE_JPG, file));
        } else {
            multipart.addFormDataPart(POST_FILENAME, file.getName(), RequestBody.create(mediaType, file));
        }
        builder.post(multipart.build());
        return verify(builder.build());
    }

    public String postFile(String url, Map<String, String> values, File file) throws NetworkException {
        return postFile(url, values, file, null);
    }

    public static String queryEncode(Map<String, String> values) {
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            try {
                query.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                query.append("=");
                query.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                query.append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return query.substring(0, query.length() - 1);
    }
}
