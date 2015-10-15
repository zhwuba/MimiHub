package com.mimi.github.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.OutOfScopeException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zwb on 15-10-15.
 */
public class AccountScope extends ScopeBase {

    private static final Key<GitHubAccount> GITHUB_ACCOUNT_KEY = Key
            .get(GitHubAccount.class);

    /**
     * Create new module
     *
     * @return module
     */
    public static Module module() {
        return new AbstractModule() {
            public void configure() {
                AccountScope scope = new AccountScope();

                bind(AccountScope.class).toInstance(scope);

                bind(GITHUB_ACCOUNT_KEY).toProvider(
                        AccountScope.<GitHubAccount> seededKeyProvider()).in(scope);
            }
        };
    }

    private final ThreadLocal<GitHubAccount> currentAccount = new ThreadLocal<GitHubAccount>();

    private final Map<GitHubAccount, Map<Key<?>, Object>> repoScopeMaps = new ConcurrentHashMap<GitHubAccount, Map<Key<?>, Object>>();

    /**
     * Enters scope using a GitHubAccount derived from the supplied account
     *
     * @param account
     * @param accountManager
     */
    public void enterWith(final Account account,
                          final AccountManager accountManager) {
        enterWith(new GitHubAccount(account, accountManager));
    }

    /**
     * Enter scope with account
     *
     * @param account
     */
    public void enterWith(final GitHubAccount account) {
        if (currentAccount.get() != null)
            throw new IllegalStateException(
                    "A scoping block is already in progress");

        currentAccount.set(account);
    }

    /**
     * Exit scope
     */
    public void exit() {
        if (currentAccount.get() == null)
            throw new IllegalStateException("No scoping block in progress");

        currentAccount.remove();
    }


    protected <T> Map<Key<?>, Object> getScopedObjectMap(final Key<T> key) {
        GitHubAccount account = currentAccount.get();
        if (account == null)
            throw new OutOfScopeException("Cannot access " + key
                    + " outside of a scoping block");

        Map<Key<?>, Object> scopeMap = repoScopeMaps.get(account);
        if (scopeMap == null) {
            scopeMap = new ConcurrentHashMap<Key<?>, Object>();
            scopeMap.put(GITHUB_ACCOUNT_KEY, account);
            repoScopeMaps.put(account, scopeMap);
        }
        return scopeMap;
    }
}
