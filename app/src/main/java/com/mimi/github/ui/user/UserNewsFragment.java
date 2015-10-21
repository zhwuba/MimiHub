package com.mimi.github.ui.user;

import android.os.Bundle;

import com.mimi.github.ui.NewsFragment;

import org.eclipse.egit.github.core.User;

/**
 * Created by zwb on 15-10-20.
 */
public abstract class UserNewsFragment extends NewsFragment
        implements OrganizationSelectionListener{

    protected User org;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        org = ((OrganizationSelectionProvider)getActivity()).addListener(this);
    }

    @Override
    public void onOrganizationSelected(User org) {
        int previousOrgId = this.org != null ? this.org.getId() : -1;
        this.org = org;
        // Only hard refresh if view already created and org is changing
        if (previousOrgId != this.org.getId())
            refreshWithProgress();
    }



    @Override
    public void onDetach() {
        OrganizationSelectionProvider selectionProvider = (OrganizationSelectionProvider) getActivity();
        if (selectionProvider != null)
            selectionProvider.removeListener(this);

        super.onDetach();

    }
}
