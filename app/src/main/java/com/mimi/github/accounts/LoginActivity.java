package com.mimi.github.accounts;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.ViewFinder;
import com.mimi.github.R;
import com.mimi.github.roboactivities.RoboActionBarAccountAuthenticatorActivity;

/**
 * Created by zwb on 15-10-14.
 */
public class LoginActivity extends RoboActionBarAccountAuthenticatorActivity{

    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

    private AutoCompleteTextView loginText;

    private EditText passwordText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        ViewFinder viewFinder = new ViewFinder(this);
        loginText = viewFinder.find(R.id.et_login);
        passwordText = viewFinder.find(R.id.et_password);


        TextView signupText = viewFinder.find(R.id.tv_signup);
        signupText.setMovementMethod(LinkMovementMethod.getInstance());
        signupText.setText(Html.fromHtml(getString(R.string.signup_link)));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu optionMenu) {
        return true;
    }
}
