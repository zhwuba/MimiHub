package com.mimi.github.ui.repo;

import android.app.Activity;

import com.mimi.github.accounts.AuthenticatedUserLoader;

import org.eclipse.egit.github.core.User;

import java.util.Collections;
import java.util.List;

/**
 * Created by zwb on 15-10-13.
 */
public class OrganizationLoader extends AuthenticatedUserLoader<List<User>> {
    private static final String TAG = "mimi.OrganizationLoader";

    public OrganizationLoader(Activity activity){
        super(activity);
    }

    protected List<User> getAccountFailureData() {
        return Collections.emptyList();
    }


}
