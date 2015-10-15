package com.mimi.github.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

/**
 * Created by zwb on 15-10-15.
 */
public abstract class FragmentPagerAdapter extends
        android.support.v4.app.FragmentPagerAdapter implements FragmentProvider{

    private final AppCompatActivity activity;

    private Fragment selected;

    public FragmentPagerAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());

        this.activity = activity;
    }


    @Override
    public Fragment getSelected() {
        return selected;
    }

    @Override
    public void setPrimaryItem(final ViewGroup container, final int position,
                               final Object object) {
        super.setPrimaryItem(container, position, object);

        boolean changed = false;
        if (object instanceof Fragment) {
            changed = object != selected;
            selected = (Fragment) object;
        } else {
            changed = object != null;
            selected = null;
        }

        if (changed)
            activity.invalidateOptionsMenu();
    }
}
