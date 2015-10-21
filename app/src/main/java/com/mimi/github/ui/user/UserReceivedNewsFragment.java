package com.mimi.github.ui.user;

import android.support.v4.content.Loader;

import com.mimi.github.core.ResourcePager;

import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.event.Event;

import java.util.List;

/**
 * Created by zwb on 15-10-15.
 */
public class UserReceivedNewsFragment extends UserNewsFragment{

    @Override
    protected ResourcePager<Event> createPager() {
        return new EventPager() {

            @Override
            public PageIterator<Event> createIterator(int page, int size) {
                return service.pageUserReceivedEvents(org.getLogin(), false,
                        page, size);
            }
        };
    }



    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {

    }


}
