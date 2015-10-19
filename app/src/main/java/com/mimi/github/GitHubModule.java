package com.mimi.github;

import android.content.Context;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mimi.github.accounts.AccountClient;
import com.mimi.github.accounts.AccountScope;
import com.mimi.github.accounts.GitHubAccount;

import org.eclipse.egit.github.core.client.GitHubClient;

import java.io.File;

/**
 * Created by zwb on 15-10-15.
 */
public class GitHubModule extends AbstractModule{
    @Override
    protected void configure() {
        install(new ServicesModule());
        install(AccountScope.module());
    }

    @Provides
    GitHubClient client(Provider<GitHubAccount> accountProvider) {
        return new AccountClient(accountProvider);
    }

    @Provides
    @Named("cacheDir")
    File cacheDir(Context context) {
        return new File(context.getFilesDir(), "cache");
    }
}
