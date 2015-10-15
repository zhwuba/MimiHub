package com.mimi.github.ui;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by zwb on 15-10-15.
 */
public abstract class PagerActivity extends DialogFragmentActivity
    implements ViewPager.OnPageChangeListener {

    private boolean menuCreated;

    /**
     * Get provider of the currently selected fragment
     *
     * @return fragment provider
     */
    protected abstract FragmentProvider getProvider();

    /**
     * Get selected fragment
     *
     * @return fragment
     */
    protected Fragment getFragment() {
        FragmentProvider provider = getProvider();
        if (provider != null)
            return provider.getSelected();
        else
            return null;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Fragment fragment = getFragment();
        if (fragment != null)
            return fragment.onOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void invalidateOptionsMenu() {
        if (menuCreated)
            super.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Fragment fragment = getFragment();
        if (fragment != null)
            fragment.onCreateOptionsMenu(menu, getMenuInflater());

        boolean created = super.onCreateOptionsMenu(menu);
        menuCreated = true;
        return created;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // Intentionally left blank
    }

    @Override
    public void onPageSelected(int position) {
        invalidateOptionsMenu();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Intentionally left blank
    }
}
