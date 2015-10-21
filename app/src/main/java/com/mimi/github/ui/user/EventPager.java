package com.mimi.github.ui.user;

import com.mimi.github.core.ResourcePager;

import org.eclipse.egit.github.core.event.Event;

/**
 * Created by zwb on 15-10-20.
 */
public abstract class EventPager extends ResourcePager<Event> {

    @Override
    protected Object getId(Event resource) {
        return resource.getId();
    }

    @Override
    protected Event register(Event resource) {
        return NewsListAdapter.isValid(resource) ? resource : null;
    }
}
