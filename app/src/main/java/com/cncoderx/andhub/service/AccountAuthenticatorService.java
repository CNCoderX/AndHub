package com.cncoderx.andhub.service;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cncoderx.andhub.account.AccountAuthenticator;

/**
 * @author cncoderx
 */
public class AccountAuthenticatorService extends Service {
    private AbstractAccountAuthenticator mAccountAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mAccountAuthenticator = new AccountAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent.getAction().equals(AccountManager.ACTION_AUTHENTICATOR_INTENT))
            return getAccountAuthenticator().getIBinder();

        return null;
    }

    public AbstractAccountAuthenticator getAccountAuthenticator() {
        return mAccountAuthenticator;
    }
}
