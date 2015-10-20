package com.mimi.github.ui;

import android.os.Bundle;

import org.eclipse.egit.github.core.event.Event;

/**
 * Created by zwb on 15-10-20.
 */
public abstract class NewsFragment extends PagedItemFragment<Event>{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
