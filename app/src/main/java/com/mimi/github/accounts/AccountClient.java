package com.mimi.github.accounts;

import android.text.TextUtils;
import android.util.Log;

import com.google.inject.Provider;
import com.mimi.github.DefaultClient;

import java.net.HttpURLConnection;

import static android.util.Log.DEBUG;

/**
 * Created by zwb on 15-10-15.
 */
public class AccountClient extends DefaultClient {

    private static final String TAG = "AccountGitHubClient";

    private final Provider<GitHubAccount> accountProvider;

    /**
     * Create account-aware client
     *
     * @param accountProvider
     */
    public AccountClient(final Provider<GitHubAccount> accountProvider) {
        super();

        this.accountProvider = accountProvider;
    }

    @Override
    protected HttpURLConnection configureRequest(final HttpURLConnection request) {
        GitHubAccount account = accountProvider.get();

        if (Log.isLoggable(TAG, DEBUG))
            Log.d(TAG, "Authenticating using " + account);

        // Credentials setting must come before super call
        String token = account.getAuthToken();
        if (!TextUtils.isEmpty(token))
            setOAuth2Token(token);
        else
            setCredentials(account.getUsername(), account.getPassword());

        return super.configureRequest(request);
    }
}
