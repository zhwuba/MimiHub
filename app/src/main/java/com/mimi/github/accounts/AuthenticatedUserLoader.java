package com.mimi.github.accounts;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.github.kevinsawicki.wishlist.AsyncLoader;

import java.io.IOException;

/**
 * Created by zwb on 15-10-13.
 */
public abstract class AuthenticatedUserLoader<D> extends AsyncLoader<D> {
    private static final String TAG = "mimi.AuthenticatedUserLoader";

    private Context  mContext;

    public AuthenticatedUserLoader(Context context){
        super(context);
        mContext = context;
    }

    protected abstract D getAccountFailureData();

    public final D loadInBackground(){
        Log.d(TAG,"------loadInBackground-----");
        final AccountManager manager = AccountManager.get(mContext);
        final Account account;
        try {
            account = AccountUtils.getAccount(manager, (Activity)mContext);
        } catch (IOException e) {
            return getAccountFailureData();
        } catch (AccountsException e) {
            return getAccountFailureData();
        }


        return null;

    }
}
