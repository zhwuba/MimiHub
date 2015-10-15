package com.mimi.github.ui.repo;

import android.accounts.Account;
import android.app.Activity;
import android.util.Log;

import com.google.inject.Provider;
import com.mimi.github.R;
import com.mimi.github.Util.ToastUtils;
import com.mimi.github.accounts.AuthenticatedUserLoader;
import com.mimi.github.core.user.UserComparator;
import com.mimi.github.persistence.AccountDataManager;

import org.eclipse.egit.github.core.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by zwb on 15-10-13.
 */
public class OrganizationLoader extends AuthenticatedUserLoader<List<User>> {
    private static final String TAG = "mimi.OrganizationLoader";

    private final Provider<UserComparator> userComparatorProvider;

    private final AccountDataManager accountDataManager;

    public OrganizationLoader(Activity activity,
                              AccountDataManager accountDataManager,
                              Provider<UserComparator> userComparatorProvider){
        super(activity);

        this.userComparatorProvider = userComparatorProvider;
        this.accountDataManager = accountDataManager;
    }

    protected List<User> getAccountFailureData() {
        return Collections.emptyList();
    }

    @Override
    public List<User> load(final Account account) {
        List<User> orgs;
        try {
            orgs = accountDataManager.getOrgs(false);
        } catch (final IOException e) {
            Log.e(TAG, "Exception loading organizations", e);
            ToastUtils.show(activity, e, R.string.error_orgs_load);
            return Collections.emptyList();
        }
        Collections.sort(orgs, userComparatorProvider.get());
        return orgs;
    }


}
