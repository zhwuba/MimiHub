package com.mimi.github.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;

import com.google.inject.Inject;

import java.io.IOException;
import java.util.concurrent.Executor;

import roboguice.inject.ContextScope;
import roboguice.util.RoboAsyncTask;

/**
 * Created by zwb on 15-10-15.
 */
public abstract class AuthenticatedUserTask<ResultT> extends
        RoboAsyncTask<ResultT> {

    @Inject
    private ContextScope contextScope;

    @Inject
    private AccountScope accountScope;

    @Inject
    private Activity activity;

    /**
     * Create asynchronous task that ensures a valid account is present when
     * executed
     *
     * @param context
     */
    protected AuthenticatedUserTask(final Context context) {
        super(context);
    }

    /**
     * Create asynchronous task that ensures a valid account is present when
     * executed
     *
     * @param context
     * @param executor
     */
    public AuthenticatedUserTask(final Context context, final Executor executor) {
        super(context, executor);
    }

    @Override
    public final ResultT call() throws Exception {
        final AccountManager manager = AccountManager.get(activity);
        final Account account = AccountUtils.getAccount(manager, activity);

        accountScope.enterWith(account, manager);
        try {
            contextScope.enter(getContext());
            try {
                return run(account);
            } catch (IOException e) {
                // Retry task if authentication failure occurs and account is
                // successfully updated
                if (AccountUtils.isUnauthorized(e)
                        && AccountUtils.updateAccount(account, activity))
                    return run(account);
                else
                    throw e;
            } finally {
                contextScope.exit(getContext());
            }
        } finally {
            accountScope.exit();
        }
    }

    /**
     * Execute task with an authenticated account
     *
     * @param account
     * @return result
     * @throws Exception
     */
    protected abstract ResultT run(Account account) throws Exception;
}
