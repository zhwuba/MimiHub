package com.mimi.github.ui.user;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.mimi.github.ui.DialogFragment;
import com.mimi.github.ui.ItemListFragment;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;

import java.util.List;

/**
 * Created by zwb on 15-10-15.
 */
public class RepositoryListFragment extends ItemListFragment<Repository> implements
        OrganizationSelectionListener{

    @Override
    protected SingleTypeAdapter<Repository> createAdapter(List<Repository> items) {
        return null;
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return 0;
    }

    @Override
    public Loader<List<Repository>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoaderReset(Loader<List<Repository>> loader) {

    }

    @Override
    public void onOrganizationSelected(User org) {

    }
}
