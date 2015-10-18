package com.mimi.github;

import android.os.Build;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import org.eclipse.egit.github.core.client.GitHubClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zwb on 15-10-15.
 */
public class DefaultClient extends GitHubClient{
    private static final String USER_AGENT = "ForkHub/1.0";

    public DefaultClient() {
        super();

        setSerializeNulls(false);
        setUserAgent(USER_AGENT);
    }
/*
    @Override
    protected HttpURLConnection createConnection(String uri) throws IOException {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
            return super.createConnection(uri);
        }

        OkUrlFactory factory = new OkUrlFactory(new OkHttpClient());
        URL url = new URL(createUri(uri));
        return factory.open(url);
    }
    */

    @Override
    protected HttpURLConnection configureRequest(HttpURLConnection request) {
        super.configureRequest(request);

        request.setRequestProperty(HEADER_ACCEPT,
                "application/vnd.github.v3.full+json");

        return request;
    }
}
