package com.mimi.github.ui;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.mimi.github.ThrowableLoader;
import com.mimi.github.core.ResourcePager;

import java.util.List;

/**
 * Created by zwb on 15-10-20.
 */
public abstract class PagedItemFragment<E> extends ItemListFragment<E> {

    protected ResourcePager<E> page;

    protected abstract ResourcePager<E> createPager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        page = createPager();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<List<E>> onCreateLoader(int id, Bundle args) {
        return new ThrowableLoader<List<E>>(getActivity(), items) {
            @Override
            public List<E> loadData() throws Exception {
                page.next();
                return page.getResources();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<E>> loader, List<E> data) {
        super.onLoadFinished(loader,data);
    }

    @Override
    protected void refreshWithProgress() {
        page.reset();
        page = createPager();

        super.refreshWithProgress();
    }
}
