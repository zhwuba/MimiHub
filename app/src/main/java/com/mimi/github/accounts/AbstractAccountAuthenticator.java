package com.mimi.github.accounts;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by zwb on 15-10-14.
 */
public abstract class AbstractAccountAuthenticator {
    private static final String TAG = "AccountAuthenticator";

    /**
     * Bundle key used for the {@code long} expiration time (in millis from the unix epoch) of the
     * associated auth token.
     *
     * @see #getAuthToken
     */
    public static final String KEY_CUSTOM_TOKEN_EXPIRY = "android.accounts.expiry";

    private final Context mContext;

    public AbstractAccountAuthenticator(Context context) {
        mContext = context;
    }

    private class Transport extends IAccountAuthenticator.Stub {
        @Override
        public void addAccount(IAccountAuthenticatorResponse response, String accountType,
                               String authTokenType, String[] features, Bundle options)
                throws RemoteException {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "addAccount: accountType " + accountType
                        + ", authTokenType " + authTokenType
                        + ", features " + (features == null ? "[]" : Arrays.toString(features)));
            }
            checkBinderPermission();
            try {
                final Bundle result = AbstractAccountAuthenticator.this.addAccount(
                        new AccountAuthenticatorResponse(response),
                        accountType, authTokenType, features, options);
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    if (result != null) {
                        result.keySet(); // force it to be unparcelled
                    }
                    Log.v(TAG, "addAccount: result " + AccountManager.sanitizeResult(result));
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                handleException(response, "addAccount", accountType, e);
            }
        }

        @Override
        public void confirmCredentials(IAccountAuthenticatorResponse response,
                                       Account account, Bundle options) throws RemoteException {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "confirmCredentials: " + account);
            }
            checkBinderPermission();
            try {
                final Bundle result = AbstractAccountAuthenticator.this.confirmCredentials(
                        new AccountAuthenticatorResponse(response), account, options);
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    if (result != null) {
                        result.keySet(); // force it to be unparcelled
                    }
                    Log.v(TAG, "confirmCredentials: result "
                            + AccountManager.sanitizeResult(result));
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                handleException(response, "confirmCredentials", account.toString(), e);
            }
        }

        @Override
        public void getAuthTokenLabel(IAccountAuthenticatorResponse response,
                                      String authTokenType)
                throws RemoteException {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "getAuthTokenLabel: authTokenType " + authTokenType);
            }
            checkBinderPermission();
            try {
                Bundle result = new Bundle();
                result.putString(AccountManager.KEY_AUTH_TOKEN_LABEL,
                        AbstractAccountAuthenticator.this.getAuthTokenLabel(authTokenType));
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    if (result != null) {
                        result.keySet(); // force it to be unparcelled
                    }
                    Log.v(TAG, "getAuthTokenLabel: result "
                            + AccountManager.sanitizeResult(result));
                }
                response.onResult(result);
            } catch (Exception e) {
                handleException(response, "getAuthTokenLabel", authTokenType, e);
            }
        }

        @Override
        public void getAuthToken(IAccountAuthenticatorResponse response,
                                 Account account, String authTokenType, Bundle loginOptions)
                throws RemoteException {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "getAuthToken: " + account
                        + ", authTokenType " + authTokenType);
            }
            checkBinderPermission();
            try {
                final Bundle result = AbstractAccountAuthenticator.this.getAuthToken(
                        new AccountAuthenticatorResponse(response), account,
                        authTokenType, loginOptions);
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    if (result != null) {
                        result.keySet(); // force it to be unparcelled
                    }
                    Log.v(TAG, "getAuthToken: result " + AccountManager.sanitizeResult(result));
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                handleException(response, "getAuthToken",
                        account.toString() + "," + authTokenType, e);
            }
        }

        @Override
        public void updateCredentials(IAccountAuthenticatorResponse response, Account account,
                                      String authTokenType, Bundle loginOptions) throws RemoteException {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "updateCredentials: " + account
                        + ", authTokenType " + authTokenType);
            }
            checkBinderPermission();
            try {
                final Bundle result = AbstractAccountAuthenticator.this.updateCredentials(
                        new AccountAuthenticatorResponse(response), account,
                        authTokenType, loginOptions);
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    // Result may be null.
                    if (result != null) {
                        result.keySet(); // force it to be unparcelled
                    }
                    Log.v(TAG, "updateCredentials: result "
                            + AccountManager.sanitizeResult(result));
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                handleException(response, "updateCredentials",
                        account.toString() + "," + authTokenType, e);
            }
        }

        @Override
        public void editProperties(IAccountAuthenticatorResponse response,
                                   String accountType) throws RemoteException {
            checkBinderPermission();
            try {
                final Bundle result = AbstractAccountAuthenticator.this.editProperties(
                        new AccountAuthenticatorResponse(response), accountType);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                handleException(response, "editProperties", accountType, e);
            }
        }

        @Override
        public void hasFeatures(IAccountAuthenticatorResponse response,
                                Account account, String[] features) throws RemoteException {
            checkBinderPermission();
            try {
                final Bundle result = AbstractAccountAuthenticator.this.hasFeatures(
                        new AccountAuthenticatorResponse(response), account, features);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                handleException(response, "hasFeatures", account.toString(), e);
            }
        }

        @Override
        public void getAccountRemovalAllowed(IAccountAuthenticatorResponse response,
                                             Account account) throws RemoteException {
            checkBinderPermission();
            try {
                final Bundle result = AbstractAccountAuthenticator.this.getAccountRemovalAllowed(
                        new AccountAuthenticatorResponse(response), account);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                handleException(response, "getAccountRemovalAllowed", account.toString(), e);
            }
        }

        @Override
        public void getAccountCredentialsForCloning(IAccountAuthenticatorResponse response,
                                                    Account account) throws RemoteException {
            checkBinderPermission();
            try {
                final Bundle result =
                        AbstractAccountAuthenticator.this.getAccountCredentialsForCloning(
                                new AccountAuthenticatorResponse(response), account);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                handleException(response, "getAccountCredentialsForCloning", account.toString(), e);
            }
        }

        @Override
        public void addAccountFromCredentials(IAccountAuthenticatorResponse response,
                                              Account account,
                                              Bundle accountCredentials) throws RemoteException {
            checkBinderPermission();
            try {
                final Bundle result =
                        AbstractAccountAuthenticator.this.addAccountFromCredentials(
                                new AccountAuthenticatorResponse(response), account,
                                accountCredentials);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                handleException(response, "addAccountFromCredentials", account.toString(), e);
            }
        }
    }

    private void handleException(IAccountAuthenticatorResponse response, String method,
                                 String data, Exception e) throws RemoteException {
        if (e instanceof NetworkErrorException) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, method + "(" + data + ")", e);
            }
            response.onError(AccountManager.ERROR_CODE_NETWORK_ERROR, e.getMessage());
        } else if (e instanceof UnsupportedOperationException) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, method + "(" + data + ")", e);
            }
            response.onError(AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION,
                    method + " not supported");
        } else if (e instanceof IllegalArgumentException) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, method + "(" + data + ")", e);
            }
            response.onError(AccountManager.ERROR_CODE_BAD_ARGUMENTS,
                    method + " not supported");
        } else {
            Log.w(TAG, method + "(" + data + ")", e);
            response.onError(AccountManager.ERROR_CODE_REMOTE_EXCEPTION,
                    method + " failed");
        }
    }

    private void checkBinderPermission() {
        final int uid = Binder.getCallingUid();
        final String perm = Manifest.permission.ACCOUNT_MANAGER;
        if (mContext.checkCallingOrSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("caller uid " + uid + " lacks " + perm);
        }
    }

    private Transport mTransport = new Transport();

    /**
     * @return the IBinder for the AccountAuthenticator
     */
    public final IBinder getIBinder() {
        return mTransport.asBinder();
    }

    /**
     * Returns a Bundle that contains the Intent of the activity that can be used to edit the
     * properties. In order to indicate success the activity should call response.setResult()
     * with a non-null Bundle.
     * @param response used to set the result for the request. If the Constants.INTENT_KEY
     *   is set in the bundle then this response field is to be used for sending future
     *   results if and when the Intent is started.
     * @param accountType the AccountType whose properties are to be edited.
     * @return a Bundle containing the result or the Intent to start to continue the request.
     *   If this is null then the request is considered to still be active and the result should
     *   sent later using response.
     */
    public abstract Bundle editProperties(AccountAuthenticatorResponse response,
                                          String accountType);

    /**
     * Adds an account of the specified accountType.
     * @param response to send the result back to the AccountManager, will never be null
     * @param accountType the type of account to add, will never be null
     * @param authTokenType the type of auth token to retrieve after adding the account, may be null
     * @param requiredFeatures a String array of authenticator-specific features that the added
     * account must support, may be null
     * @param options a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of
     * the account that was added, or
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     * @throws NetworkErrorException if the authenticator could not honor the request due to a
     * network error
     */
    public abstract Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                                      String authTokenType, String[] requiredFeatures, Bundle options)
            throws NetworkErrorException;

    /**
     * Checks that the user knows the credentials of an account.
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account whose credentials are to be checked, will never be null
     * @param options a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_BOOLEAN_RESULT}, true if the check succeeded, false otherwise
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     * @throws NetworkErrorException if the authenticator could not honor the request due to a
     * network error
     */
    public abstract Bundle confirmCredentials(AccountAuthenticatorResponse response,
                                              Account account, Bundle options)
            throws NetworkErrorException;

    /**
     * Gets an authtoken for an account.
     *
     * If not {@code null}, the resultant {@link Bundle} will contain different sets of keys
     * depending on whether a token was successfully issued and, if not, whether one
     * could be issued via some {@link android.app.Activity}.
     * <p>
     * If a token cannot be provided without some additional activity, the Bundle should contain
     * {@link AccountManager#KEY_INTENT} with an associated {@link Intent}. On the other hand, if
     * there is no such activity, then a Bundle containing
     * {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} should be
     * returned.
     * <p>
     * If a token can be successfully issued, the implementation should return the
     * {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of the
     * account associated with the token as well as the {@link AccountManager#KEY_AUTHTOKEN}. In
     * addition {@link AbstractAccountAuthenticator} implementations that declare themselves
     * {@code android:customTokens=true} may also provide a non-negative {@link
     * #KEY_CUSTOM_TOKEN_EXPIRY} long value containing the expiration timestamp of the expiration
     * time (in millis since the unix epoch).
     * <p>
     * Implementers should assume that tokens will be cached on the basis of account and
     * authTokenType. The system may ignore the contents of the supplied options Bundle when
     * determining to re-use a cached token. Furthermore, implementers should assume a supplied
     * expiration time will be treated as non-binding advice.
     * <p>
     * Finally, note that for android:customTokens=false authenticators, tokens are cached
     * indefinitely until some client calls {@link
     * AccountManager#invalidateAuthToken(String,String)}.
     *
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account whose credentials are to be retrieved, will never be null
     * @param authTokenType the type of auth token to retrieve, will never be null
     * @param options a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response.
     * @throws NetworkErrorException if the authenticator could not honor the request due to a
     * network error
     */
    public abstract Bundle getAuthToken(AccountAuthenticatorResponse response,
                                        Account account, String authTokenType, Bundle options)
            throws NetworkErrorException;

    /**
     * Ask the authenticator for a localized label for the given authTokenType.
     * @param authTokenType the authTokenType whose label is to be returned, will never be null
     * @return the localized label of the auth token type, may be null if the type isn't known
     */
    public abstract String getAuthTokenLabel(String authTokenType);

    /**
     * Update the locally stored credentials for an account.
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account whose credentials are to be updated, will never be null
     * @param authTokenType the type of auth token to retrieve after updating the credentials,
     * may be null
     * @param options a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of
     * the account whose credentials were updated, or
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     * @throws NetworkErrorException if the authenticator could not honor the request due to a
     * network error
     */
    public abstract Bundle updateCredentials(AccountAuthenticatorResponse response,
                                             Account account, String authTokenType, Bundle options) throws NetworkErrorException;

    /**
     * Checks if the account supports all the specified authenticator specific features.
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account to check, will never be null
     * @param features an array of features to check, will never be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_BOOLEAN_RESULT}, true if the account has all the features,
     * false otherwise
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     * @throws NetworkErrorException if the authenticator could not honor the request due to a
     * network error
     */
    public abstract Bundle hasFeatures(AccountAuthenticatorResponse response,
                                       Account account, String[] features) throws NetworkErrorException;

    /**
     * Checks if the removal of this account is allowed.
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account to check, will never be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_BOOLEAN_RESULT}, true if the removal of the account is
     * allowed, false otherwise
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     * @throws NetworkErrorException if the authenticator could not honor the request due to a
     * network error
     */
    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response,
                                           Account account) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
        return result;
    }

    /**
     * Returns a Bundle that contains whatever is required to clone the account on a different
     * user. The Bundle is passed to the authenticator instance in the target user via
     * {@link #addAccountFromCredentials(AccountAuthenticatorResponse, Account, Bundle)}.
     * The default implementation returns null, indicating that cloning is not supported.
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account to clone, will never be null
     * @return a Bundle result or null if the result is to be returned via the response.
     * @throws NetworkErrorException
     * @see {@link #addAccountFromCredentials(AccountAuthenticatorResponse, Account, Bundle)}
     */
    public Bundle getAccountCredentialsForCloning(final AccountAuthenticatorResponse response,
                                                  final Account account) throws NetworkErrorException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle result = new Bundle();
                result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
                response.onResult(result);
            }
        }).start();
        return null;
    }

    /**
     * Creates an account based on credentials provided by the authenticator instance of another
     * user on the device, who has chosen to share the account with this user.
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account to clone, will never be null
     * @param accountCredentials the Bundle containing the required credentials to create the
     * account. Contents of the Bundle are only meaningful to the authenticator. This Bundle is
     * provided by {@link #getAccountCredentialsForCloning(AccountAuthenticatorResponse, Account)}.
     * @return a Bundle result or null if the result is to be returned via the response.
     * @throws NetworkErrorException
     * @see {@link #getAccountCredentialsForCloning(AccountAuthenticatorResponse, Account)}
     */
    public Bundle addAccountFromCredentials(final AccountAuthenticatorResponse response,
                                            Account account,
                                            Bundle accountCredentials) throws NetworkErrorException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle result = new Bundle();
                result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
                response.onResult(result);
            }
        }).start();
        return null;
    }
}
