package com.mimi.github.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.mimi.github.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by zwb on 15-10-19.
 */
public abstract class ItemListFragment<E> extends DialogFragment
            implements SwipeRefreshLayout.OnRefreshListener, LoaderCallbacks<List<E>>{
    private final static String TAG = "ItemListFragment";

    protected List<E> items = Collections.emptyList();

    protected ListView listView;

    protected TextView empityView;

    protected ProgressBar progressBar;



    private SwipeRefreshLayout swipeRefreshLayout;


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

        configureList(getActivity(),listView);
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

    }
}
