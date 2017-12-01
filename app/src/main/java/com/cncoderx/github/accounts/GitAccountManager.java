package com.cncoderx.github.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.cncoderx.github.utils.Constants;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author cncoderx
 */
public class GitAccountManager {
    private static AccountManager sAccountManager;
    private static HashMap<String, Account> sAccountPools;

    public static void initialize(Context context) {
        sAccountManager = AccountManager.get(context);
        sAccountPools = new HashMap<>();
    }

    public static GitAccount[] getGitAccounts() {
        Account[] accounts = sAccountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
        int length = accounts.length;
        if (length > 0) {
            GitAccount[] gitAccounts = new GitAccount[length];
            int index = 0;
            for (Account account : accounts) {
                GitAccount gitAccount = new GitAccount();
                gitAccount.name = account.name;
                gitAccount.password = sAccountManager.getPassword(account);
                gitAccount.token = sAccountManager.peekAuthToken(account, Constants.TOKEN_TYPE);
                gitAccount.avatar = sAccountManager.getUserData(account, ACCOUNT_AVATAR);
                String updated = sAccountManager.getUserData(account, ACCOUNT_UPDATED);
                gitAccount.updated = TextUtils.isEmpty(updated) ? 0 : Long.parseLong(updated);
                gitAccounts[index++] = gitAccount;
            }
            Arrays.sort(gitAccounts);
            return gitAccounts;
        }
        return new GitAccount[0];
    }

    public static boolean addGitAccount(String user, String pwd, String token) {
        Account account = obtainAccount(user);
        boolean result = sAccountManager.addAccountExplicitly(account, pwd, null);
        if (result) {
            sAccountManager.setAuthToken(account, Constants.TOKEN_TYPE, token);
            sAccountManager.setUserData(account, ACCOUNT_UPDATED, Long.toString(System.currentTimeMillis()));
        }
        return result;
    }

    public static void updateGitAccount(String user, String pwd, String token) {
        Account account = obtainAccount(user);
        sAccountManager.setPassword(account, pwd);
        sAccountManager.setAuthToken(account, Constants.TOKEN_TYPE, token);
        sAccountManager.setUserData(account, ACCOUNT_UPDATED, Long.toString(System.currentTimeMillis()));
    }

    public static void removeGitAccount(Activity activity, String user) {
        Account account = obtainAccount(user);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            sAccountManager.removeAccount(account, activity, null, null);
        } else {
            sAccountManager.removeAccount(account, null, null);
        }
    }

    public static void updateGitAccountAvatar(String user, String avatar) {
        Account account = obtainAccount(user);
        sAccountManager.setUserData(account, ACCOUNT_AVATAR, avatar);
    }

    public static void getGitAccountAvatar(String user) {
        Account account = obtainAccount(user);
        sAccountManager.getUserData(account, ACCOUNT_AVATAR);
    }

    public static String getGitAccountToken(String user) {
        Account account = obtainAccount(user);
        return sAccountManager.peekAuthToken(account, Constants.TOKEN_TYPE);
    }

    public static void invalidateAuthToken(String token) {
        sAccountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE, token);
    }

    private static Account obtainAccount(String user) {
        Account account = sAccountPools.get(user);
        if (account == null) {
            account = new Account(user, Constants.ACCOUNT_TYPE);
            sAccountPools.put(user, account);
        }
        return account;
    }

    static final String ACCOUNT_AVATAR = "avatar";
    static final String ACCOUNT_UPDATED = "updated";

}
