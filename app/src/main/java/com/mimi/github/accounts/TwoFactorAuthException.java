package com.mimi.github.accounts;

import java.io.IOException;

/**
 * Created by zwb on 15-10-15.
 */
public class TwoFactorAuthException extends IOException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3889626691109709714L;

    /**
     * Cause exception
     */
    protected final IOException cause;

    /**
     * Two-factor authentication type
     */
    protected final int twoFactorAuthType;

    /**
     * Create two-factor authentification exception
     *
     * @param cause
     * @param twoFactorAuthType
     */
    public TwoFactorAuthException(IOException cause, int twoFactorAuthType) {
        this.cause = cause;
        this.twoFactorAuthType = twoFactorAuthType;
    }

    @Override
    public String getMessage() {
        return cause != null ? cause.getMessage() : super.getMessage();
    }

    @Override
    public IOException getCause() {
        return cause;
    }
}
