package com.mimi.github.accounts;

import android.text.TextUtils;
import android.util.Log;

import com.mimi.github.DefaultClient;

import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.GitHubResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

/**
 * Created by zwb on 15-10-15.
 */
public class TwoFactorAuthClient extends DefaultClient {
    private final static String TAG = "TwoFactorAuthClient";

    /**
     * Two-factor authentication code header
     */
    protected static final String HEADER_OTP = "X-GitHub-OTP";

    /**
     * Two-factor authentication type by application
     */
    public static final int TWO_FACTOR_AUTH_TYPE_APP = 1001;

    /**
     * Two-factor authentication type by sms
     */
    public static final int TWO_FACTOR_AUTH_TYPE_SMS = 1002;

    private String otpCode;

    public TwoFactorAuthClient() {
        super();
    }

    /**
     * Set OTP code which will be added to POST requests
     *
     * @param otpCode
     */
    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    /**
     * Get response from URI and bind to specified type
     *
     * @param request
     * @return response
     * @throws java.io.IOException
     */
    @Override
    public GitHubResponse get(GitHubRequest request) throws IOException {
        HttpURLConnection httpRequest = createGet(request.generateUri());
        if (!TextUtils.isEmpty(otpCode))
            httpRequest.setRequestProperty(HEADER_OTP, otpCode);

        try {
            String accept = request.getResponseContentType();
            if (accept != null)
                httpRequest.setRequestProperty(HEADER_ACCEPT, accept);
            final int code = httpRequest.getResponseCode();
            updateRateLimits(httpRequest);
            if (isOk(code))
                return new GitHubResponse(httpRequest, getBody(request,
                        getStream(httpRequest)));
            if (isEmpty(code))
                return new GitHubResponse(httpRequest, null);
            throw createException(getStream(httpRequest), code,
                    httpRequest.getResponseMessage());
        } catch (IOException e) {
            throw checkTwoFactorAuthError(httpRequest, e);
        }
    }

    /**
     * Post data to URI
     *
     * @param <V>
     * @param uri
     * @param params
     * @param type
     * @return response
     * @throws IOException
     */
    @Override
    public <V> V post(final String uri, final Object params, final Type type)
            throws IOException {
        HttpURLConnection request = createPost(uri);
        if (!TextUtils.isEmpty(otpCode))
            request.setRequestProperty(HEADER_OTP, otpCode);

        try {
            return sendJson(request, params, type);
        } catch (IOException e) {
            throw checkTwoFactorAuthError(request, e);
        }
    }

    private IOException checkTwoFactorAuthError(HttpURLConnection request, IOException e) throws IOException {
        String otpHeader = request.getHeaderField(HEADER_OTP);
        if (!TextUtils.isEmpty(otpHeader) && otpHeader.contains("required"))
            return createTwoFactorAuthException(e, otpHeader);
        else
            return e;
    }

    private TwoFactorAuthException createTwoFactorAuthException(
            IOException cause, String otpHeader) {
        int twoFactorAuthType = -1;
        if (otpHeader.contains("app"))
            twoFactorAuthType = TWO_FACTOR_AUTH_TYPE_APP;
        else if (otpHeader.contains("sms"))
            twoFactorAuthType = TWO_FACTOR_AUTH_TYPE_SMS;

        return new TwoFactorAuthException(cause, twoFactorAuthType);
    }

    private <V> V sendJson(final HttpURLConnection request,
                           final Object params, final Type type) throws IOException {
        sendParams(request, params);
        final int code = request.getResponseCode();
        updateRateLimits(request);
        if (isOk(code))
            if (type != null)
                return parseJson(getStream(request), type);
            else
                return null;
        if (isEmpty(code))
            return null;
        throw createException(getStream(request), code,
                request.getResponseMessage());
    }
}
