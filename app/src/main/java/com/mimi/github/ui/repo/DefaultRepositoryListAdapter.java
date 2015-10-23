package com.mimi.github.ui.repo;

import android.accounts.Account;
import android.view.LayoutInflater;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by zhwuba on 15-10-23.
 */
public class DefaultRepositoryListAdapter extends RepositoryListAdapter<Repository> {
    private static final String TAG = "DefaultRepository";

    private AtomicReference<User> account;

   public DefaultRepositoryListAdapter(LayoutInflater inflater,
        Repository[] repositories, AtomicReference<User> account){
       super(0,inflater, repositories);

       this.account = account;
   }

    @Override
    protected int[] getChildViewIds() {
        return new int[0];
    }

    @Override
    protected void update(int i, Repository repository) {

    }
}
