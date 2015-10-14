package com.mimi.github.accounts;

import android.os.Bundle;

import com.mimi.github.R;
import com.mimi.github.roboactivities.RoboActionBarAccountAuthenticatorActivity;

/**
 * Created by zwb on 15-10-14.
 */
public class LoginActivity extends RoboActionBarAccountAuthenticatorActivity{

    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
    }
}
