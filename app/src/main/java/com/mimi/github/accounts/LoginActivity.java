package com.mimi.github.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.ViewFinder;
import com.google.inject.Inject;
import com.mimi.github.R;
import com.mimi.github.Util.ToastUtils;
import com.mimi.github.persistence.AccountDataManager;
import com.mimi.github.roboactivities.RoboActionBarAccountAuthenticatorActivity;
import com.mimi.github.ui.LightProgressDialog;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

import java.io.IOException;
import java.util.List;

import roboguice.util.RoboAsyncTask;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;
import static android.accounts.AccountManager.KEY_AUTHTOKEN;
import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;
import static com.mimi.github.accounts.AccountConstants.ACCOUNT_TYPE;
import static com.mimi.github.accounts.AccountConstants.PROVIDER_AUTHORITY;

/**
 * Created by zwb on 15-10-14.
 */
public class LoginActivity extends RoboActionBarAccountAuthenticatorActivity{

    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

    /**
     * Initial user name
     */
    public static final String PARAM_USERNAME = "username";

    private static final String PARAM_CONFIRMCREDENTIALS = "confirmCredentials";

    private static final String TAG = "LoginActivity";

    private AutoCompleteTextView loginText;

    private EditText passwordText;

    private String username;

    private String password;

    private String authTokenType;

    private boolean confirmCredentials = false;

    protected boolean requestNewAccount = false;

    private MenuItem loginItem;

    private RoboAsyncTask<User> authenticationTask;

    private AccountManager accountManager;

    /**
     * Sync period in seconds, currently every 8 hours
     */
    private static final long SYNC_PERIOD = 8L * 60L * 60L;

    public static void configureSyncFor(Account account) {
        Log.d(TAG, "Configuring account sync");

        ContentResolver.setIsSyncable(account, PROVIDER_AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(account, PROVIDER_AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, PROVIDER_AUTHORITY,
                new Bundle(), SYNC_PERIOD);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        accountManager = AccountManager.get(this);

        ViewFinder viewFinder = new ViewFinder(this);
        loginText = viewFinder.find(R.id.et_login);
        passwordText = viewFinder.find(R.id.et_password);


        TextView signupText = viewFinder.find(R.id.tv_signup);
        signupText.setMovementMethod(LinkMovementMethod.getInstance());
        signupText.setText(Html.fromHtml(getString(R.string.signup_link)));

        final Intent intent = getIntent();
        username = intent.getStringExtra(PARAM_USERNAME);
        authTokenType = intent.getStringExtra(PARAM_AUTHTOKEN_TYPE);
        confirmCredentials = intent.getBooleanExtra(PARAM_CONFIRMCREDENTIALS, false);
        //requestNewAccount = (username == null);

        if(!TextUtils.isEmpty(username)){
            loginText.setText(username);
            loginText.setEnabled(false);
            loginText.setFocusable(false);
        }

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateEnablement();
            }
        };

        loginText.addTextChangedListener(watcher);
        passwordText.addTextChangedListener(watcher);

    }

    @Override
    public void onResume(){
        super.onResume();

        // Finish task if valid account exists
        if (requestNewAccount) {
            Account existing = AccountUtils.getPasswordAccessibleAccount(this);
            if (existing != null && !TextUtils.isEmpty(existing.name)) {
                String password = AccountManager.get(this)
                        .getPassword(existing);
                if (!TextUtils.isEmpty(password))
                    finishLogin(existing.name, password);
            }
            return;
        }

        updateEnablement();
    }

    private boolean loginEnabled() {
        return !TextUtils.isEmpty(loginText.getText())
                && !TextUtils.isEmpty(passwordText.getText());
    }

    private void updateEnablement() {
        if (loginItem != null)
            loginItem.setEnabled(loginEnabled());
    }

    private void handleLogin(){
        if (requestNewAccount)
            username = loginText.getText().toString();
        password = passwordText.getText().toString();

        final AlertDialog dialog = LightProgressDialog.create(this,
                R.string.login_activity_authenticating);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (authenticationTask != null)
                  authenticationTask.cancel(true);
            }
        });
        dialog.show();

        authenticationTask = new RoboAsyncTask<User>(this) {
            @Override
            public User call() throws Exception {
                GitHubClient client = new TwoFactorAuthClient();
                client.setCredentials(username,password);
                //client.setOAuth2Token("5495f465e05900884b28518023d5ea44b9178833");

                User user;
                try{
                        user = new UserService(client).getUser();
                }catch (TwoFactorAuthException ex){
                        Log.e(TAG," -----getuser null");
                    return null;
                }
                Log.d(TAG,"-----login success---user login------" + user.getLogin());
                Account account = new Account(user.getLogin(),ACCOUNT_TYPE);
                if (requestNewAccount) {
                    accountManager
                            .addAccountExplicitly(account, password, null);
                    configureSyncFor(account);
                    try {
                        new AccountLoader(LoginActivity.this).call();
                    } catch (IOException e) {
                        Log.d(TAG, "Exception loading organizations", e);
                    }
                } else
                    accountManager.setPassword(account, password);

                return user;
            }

            @Override
            protected void onSuccess(User user) throws Exception {
                Log.d(TAG,"-----login success---user------" + user.getName());
                dialog.dismiss();

                if (user != null)
                    onAuthenticationResult(true);
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Log.d(TAG,"-----login onException---------" + e);
                dialog.dismiss();

                handleLoginException(e);
            }
        };

        authenticationTask.execute();
    }

    public void onAuthenticationResult(boolean result) {
        if (result) {
            if (!confirmCredentials)
                finishLogin(username, password);
            else
                finishConfirmCredentials(true);
        } else {
            if (requestNewAccount)
                ToastUtils.show(this, R.string.invalid_login_or_password);
            else
                ToastUtils.show(this, R.string.invalid_password);
        }
    }

    private void handleLoginException(final Exception e) {
        if (AccountUtils.isUnauthorized(e))
            onAuthenticationResult(false);
        else
            ToastUtils.show(LoginActivity.this, e, R.string.code_authentication_failed);
    }

    protected void finishConfirmCredentials(boolean result) {
        final Account account = new Account(username, ACCOUNT_TYPE);
        accountManager.setPassword(account, password);

        final Intent intent = new Intent();
        intent.putExtra(KEY_BOOLEAN_RESULT, result);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void finishLogin(final String username, final String password) {
        final Intent intent = new Intent();
        intent.putExtra(KEY_ACCOUNT_NAME, username);
        intent.putExtra(KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
        if (ACCOUNT_TYPE.equals(authTokenType))
            intent.putExtra(KEY_AUTHTOKEN, password);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu optionMenu) {
        getMenuInflater().inflate(R.menu.login,optionMenu);
        loginItem = optionMenu.findItem(R.id.m_login);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.m_login:
                handleLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class AccountLoader extends
            AuthenticatedUserTask<List<User>> {

        @Inject
        private AccountDataManager cache;

        protected AccountLoader(Context context) {
            super(context);
        }

        @Override
        protected List<User> run(Account account) throws Exception {
            return cache.getOrgs(true);
        }
    }
}
