package com.mimi.github.accounts;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static android.accounts.AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE;
import static android.accounts.AccountManager.KEY_INTENT;
import static com.mimi.github.accounts.LoginActivity.PARAM_AUTHTOKEN_TYPE;

/**
 * Created by zwb on 15-10-14.
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

    private Context context;

    public AccountAuthenticator(final Context context) {
        super(context);

        this.context = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response,
                             String accountType, String authTokenType,
                             String[] requiredFeatures, Bundle options)
            throws NetworkErrorException {

        final Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
