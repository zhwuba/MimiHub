package com.mimi.github.ui;

import android.os.Bundle;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.google.inject.Inject;
import com.mimi.github.R;
import com.mimi.github.Util.AvatarLoader;
import com.mimi.github.ui.user.NewsListAdapter;

import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.service.EventService;

import java.util.List;

/**
 * Created by zwb on 15-10-20.
 */
public abstract class NewsFragment extends PagedItemFragment<Event>{

    @Inject
    private AvatarLoader avatarLoader;

    @Inject
    protected EventService service;

    private boolean showRepoName = true;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_news);
    }

    @Override
    protected SingleTypeAdapter<Event> createAdapter(List<Event> items) {
        return new NewsListAdapter(getActivity().getLayoutInflater(),
                items.toArray(new Event[items.size()]), avatarLoader, showRepoName);
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_news_load;
    }

    protected void setShowRepoName(boolean showRepoName){
        this.showRepoName = showRepoName;
    }


}
