package com.mimi.github.accounts;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.github.kevinsawicki.wishlist.AsyncLoader;
import com.google.inject.Inject;

import java.io.IOException;

import roboguice.RoboGuice;
import roboguice.inject.ContextScope;

/**
 * Created by zwb on 15-10-13.
 */
public abstract class AuthenticatedUserLoader<D> extends AsyncLoader<D> {
    private static final String TAG = "mimi.AuthenticatedUserr";

    @Inject
    private ContextScope contextScope;

    @Inject
    private AccountScope accountScope;

    @Inject
    protected Activity activity;

    private Context  mContext;

    public AuthenticatedUserLoader(Context context){
        super(context);

        RoboGuice.injectMembers(context, this);
    }

    protected abstract D getAccountFailureData();

    public final D loadInBackground(){
        Log.d(TAG,"------loadInBackground-----");
        final AccountManager manager = AccountManager.get(activity);
        final Account account;
        try {
            account = AccountUtils.getAccount(manager, activity);
        } catch (IOException e) {
            return getAccountFailureData();
        } catch (AccountsException e) {
            return getAccountFailureData();
        }

        accountScope.enterWith(account, manager);
        try {
            contextScope.enter(getContext());
            try {
                return load(account);
            } finally {
                contextScope.exit(getContext());
            }
        } finally {
            accountScope.exit();
        }

    }

    public abstract D load(Account account);
}
