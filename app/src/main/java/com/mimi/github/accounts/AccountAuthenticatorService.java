package com.mimi.github.accounts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import static android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT;

/**
 * Created by zwb on 15-10-14.
 */
public class AccountAuthenticatorService extends Service{

    private static AccountAuthenticator AUTHENTICATOR;

    public IBinder onBind(Intent intent){
        return intent.getAction().equals(ACTION_AUTHENTICATOR_INTENT) ?
                getAuthenticator().getIBinder() : null;
    }

    private AccountAuthenticator getAuthenticator() {
        if (AUTHENTICATOR == null)
            AUTHENTICATOR = new AccountAuthenticator(this);
        return AUTHENTICATOR;
    }
}
