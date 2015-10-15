package com.mimi.github.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.github.kevinsawicki.wishlist.ViewFinder;
import com.mimi.github.roboactivities.RoboSupportFragment;

import java.io.Serializable;

/**
 * Created by zwb on 15-10-15.
 */
public abstract class DialogFragment extends RoboSupportFragment
        implements DialogResultListener{

    protected ViewFinder finder;

    protected boolean isUsable(){
        return getActivity() != null;
    }

    protected < V extends Serializable > V getSerializableExtra(final String name){
        Activity activity = getActivity();
        if(activity != null){
            return (V)activity.getIntent().getSerializableExtra(name);
        }
        return null;
    }

    protected String getStringExtra(final String name){
        Activity activity = getActivity();
        if(activity != null){
            return activity.getIntent().getStringExtra(name);
        }
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        finder = new ViewFinder(view);
    }

    @Override
    public void onDialogResult(int requestCode, int resultCode, Bundle arguments) {

    }
}
