package com.mimi.github.ui;

import android.os.Bundle;

import com.mimi.github.core.ResourcePager;

/**
 * Created by zwb on 15-10-20.
 */
public abstract class PagedItemFragment<E> extends ItemListFragment<E> {

    protected ResourcePager<E> page;

    protected abstract ResourcePager<E> createPager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
