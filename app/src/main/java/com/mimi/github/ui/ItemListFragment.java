package com.mimi.github.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.ViewUtils;
import com.mimi.github.R;
import com.mimi.github.ThrowableLoader;
import com.mimi.github.Util.ToastUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by zwb on 15-10-19.
 */
public abstract class ItemListFragment<E> extends DialogFragment
            implements SwipeRefreshLayout.OnRefreshListener, LoaderCallbacks<List<E>>{
    private final static String TAG = "ItemListFragment";

    private static final String FORCE_REFRESH = "forceRefresh";

    protected List<E> items = Collections.emptyList();

    protected ListView listView;

    protected TextView empityView;

    protected ProgressBar progressBar;



    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean listShown;

    protected void forceRefresh(){
        Bundle bundle = new Bundle();
        bundle.putBoolean(FORCE_REFRESH, true);
        refresh(bundle);
    }

    public void refresh() {
        refresh(null);
    }

    private void refresh(final Bundle args) {
        if (!isUsable())
            return;

        getLoaderManager().restartLoader(0, args, this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0,null,this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_list,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_item);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.pager_title_background_top_start,
                R.color.pager_title_background_end,
                R.color.text_link,
                R.color.pager_title_background_end);
        swipeRefreshLayout.setOnRefreshListener(this);

        listView = (ListView)view.findViewById(android.R.id.list);

        empityView = (TextView)view.findViewById(R.id.textView);

        progressBar = (ProgressBar)view.findViewById(R.id.pb_loading);

        configureList(getActivity(), listView);
    }

    protected ItemListFragment<E> setEmptyText(int resouceId){
        if(empityView != null){
            empityView.setText(resouceId);
        }
        return this;
    }

    protected abstract SingleTypeAdapter<E> createAdapter(final List<E> items);

    public ListView getListView() {
        return listView;
    }

    protected HeaderFooterListAdapter<SingleTypeAdapter<E>> createAdapter() {
        SingleTypeAdapter<E> wrapped = createAdapter(items);
        return new HeaderFooterListAdapter<SingleTypeAdapter<E>>(getListView(),
                wrapped);
    }

    protected void configureList(Activity activity, ListView listview){
        listview.setAdapter(createAdapter());
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "---------onRefresh-------");
        forceRefresh();
    }

    protected abstract int getErrorMessage(Exception exception);

    @Override
    public void onLoadFinished(Loader<List<E>> loader, List<E> data) {
        if (!isUsable())
            return;

        swipeRefreshLayout.setRefreshing(false);
        Exception exception = getException(loader);
        if (exception != null) {
            showError(exception, getErrorMessage(exception));
            showList();
            return;
        }

        this.items = data;
        getListAdapter().getWrappedAdapter().setItems(data.toArray());
        showList();
    }

    protected HeaderFooterListAdapter<SingleTypeAdapter<E>> getListAdapter() {
        if (listView != null)
            return (HeaderFooterListAdapter<SingleTypeAdapter<E>>) listView
                    .getAdapter();
        else
            return null;
    }


    protected void showList() {
        setListShown(true, isResumed());
    }


    protected void refreshWithProgress(){

    }

    protected void showError(final Exception e, final int defaultMessage) {
        ToastUtils.show(getActivity(), e, defaultMessage);
    }

    protected Exception getException(final Loader<List<E>> loader) {
        if (loader instanceof ThrowableLoader)
            return ((ThrowableLoader<List<E>>) loader).clearException();
        else
            return null;
    }

    private ItemListFragment<E> fadeIn(final View view, final boolean animate) {
        if (view != null)
            if (animate)
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_in));
            else
                view.clearAnimation();
        return this;
    }

    private ItemListFragment<E> show(final View view) {
        ViewUtils.setGone(view, false);
        return this;
    }

    private ItemListFragment<E> hide(final View view) {
        ViewUtils.setGone(view, true);
        return this;
    }

    public ItemListFragment<E> setListShown(final boolean shown,
                                            final boolean animate) {
        if (!isUsable())
            return this;

        if (shown == listShown) {
            if (shown)
                // List has already been shown so hide/show the empty view with
                // no fade effect
                if (items.isEmpty())
                    hide(listView).show(empityView);
                else
                    hide(empityView).show(listView);
            return this;
        }

        listShown = shown;

        if (shown)
            if (!items.isEmpty())
                hide(progressBar).hide(empityView).fadeIn(listView, animate)
                        .show(listView);
            else
                hide(progressBar).hide(listView).fadeIn(empityView, animate)
                        .show(empityView);
        else
            hide(listView).hide(empityView).fadeIn(progressBar, animate)
                    .show(progressBar);

        return this;
    }
}
