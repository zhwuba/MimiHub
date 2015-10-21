package com.mimi.github;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

import com.mimi.github.accounts.AccountUtils;
import com.mimi.github.accounts.AuthenticatedUserLoader;

/**
 * Created by zwb on 15-10-21.
 */
public abstract class ThrowableLoader<D> extends AuthenticatedUserLoader<D> {
    private static final String TAG = "ThrowableLoader";

    private D data;

    private Exception exception;

    public ThrowableLoader(Context context,D data){
        super(context);

        this.data = data;
    }

    @Override
    protected D getAccountFailureData() {
        return data;
    }

    public Exception clearException() {
        final Exception throwable = exception;
        exception = null;
        return throwable;
    }

    @Override
    public D load(Account account) {
        exception = null;
        try {
            return loadData();
        } catch (Exception e) {
            if (AccountUtils.isUnauthorized(e)
                    && AccountUtils.updateAccount(account, activity))
                try {
                    return loadData();
                } catch (Exception e2) {
                    e = e2;
                }
            Log.d(TAG, "Exception loading data", e);
            exception = e;
            return data;
        }
    }

    public abstract D loadData() throws Exception;
}
