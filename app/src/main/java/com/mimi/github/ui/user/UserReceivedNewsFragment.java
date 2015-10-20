package com.mimi.github.ui.user;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.mimi.github.core.ResourcePager;

import org.eclipse.egit.github.core.event.Event;

import java.util.List;

/**
 * Created by zwb on 15-10-15.
 */
public class UserReceivedNewsFragment extends UserNewsFragment{

    @Override
    protected ResourcePager<Event> createPager() {
        return null;
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {

    }

    @Override
    protected SingleTypeAdapter<Event> createAdapter(List<Event> items) {
        return null;
    }
}
