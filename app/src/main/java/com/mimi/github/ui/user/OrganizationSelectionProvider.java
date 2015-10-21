package com.mimi.github.ui.user;

import org.eclipse.egit.github.core.User;

/**
 * Created by zwb on 15-10-21.
 */
public interface OrganizationSelectionProvider {
    User addListener(OrganizationSelectionListener listener);
    OrganizationSelectionProvider removeListener(OrganizationSelectionListener listener);
}
