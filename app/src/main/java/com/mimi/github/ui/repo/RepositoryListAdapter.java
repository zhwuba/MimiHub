package com.mimi.github.ui.repo;

import android.view.LayoutInflater;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

/**
 * Created by zhwuba on 15-10-23.
 */
public abstract class RepositoryListAdapter<V> extends SingleTypeAdapter<V> {
    
    public RepositoryListAdapter(int viewId, LayoutInflater inflater, Object[] items){
        super(inflater, viewId);
        
        setItems(items);
    }
}
