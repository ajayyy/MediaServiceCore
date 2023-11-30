package com.liskovsoft.youtubeapi.service;

import androidx.annotation.Nullable;
import com.liskovsoft.mediaserviceinterfaces.SignInService;
import com.liskovsoft.mediaserviceinterfaces.data.Account;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.auth.V2.AuthService;
import com.liskovsoft.youtubeapi.auth.models.auth.AccessToken;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.youtubeapi.service.data.YouTubeAccount;
import com.liskovsoft.youtubeapi.service.internal.YouTubeAccountManager;
import io.reactivex.Observable;

import java.util.List;
import java.util.Map;

public class YouTubeSignInService implements SignInService {
    private static final String TAG = YouTubeSignInService.class.getSimpleName();
    private static final long TOKEN_REFRESH_PERIOD_MS = 60 * 60 * 1_000; // NOTE: auth token max lifetime is 60 min
    private static YouTubeSignInService sInstance;
    private final AuthService mAuthService;
    private final YouTubeAccountManager mAccountManager;
    private String mCachedAuthorizationHeader;
    private String mCachedAuthorizationHeader2;
    private long mLastUpdateTime;

    private YouTubeSignInService() {
        mAuthService = AuthService.instance();
        mAccountManager = YouTubeAccountManager.instance(this);

        GlobalPreferences.setOnInit(() -> {
            mAccountManager.init();
            try {
                updateAuthHeaders();
            } catch (Exception e) {
                // Host not found
                e.printStackTrace();
            }
        });
    }

    public static YouTubeSignInService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeSignInService();
        }

        return sInstance;
    }

    @Override
    public Observable<String> signInObserve() {
        return mAccountManager.signInObserve();
    }

    @Override
    public void signOut() {
        // TODO: not implemented
    }

    @Override
    public Observable<Void> signOutObserve() {
        return RxHelper.create(emitter -> {
            signOut();
            emitter.onComplete();
        });
    }

    public void checkAuth() {
        updateAuthHeaders();
    }

    private void updateAuthHeaders() {
        if (mCachedAuthorizationHeader != null && System.currentTimeMillis() - mLastUpdateTime < TOKEN_REFRESH_PERIOD_MS) {
            return;
        }

        Account account = mAccountManager.getSelectedAccount();
        String refreshToken = account != null ? ((YouTubeAccount) account).getRefreshToken() : null;
        String refreshToken2 = account != null ? ((YouTubeAccount) account).getRefreshToken2() : null;
        // get or create authorization on fly
        mCachedAuthorizationHeader = createAuthorizationHeader(refreshToken);
        mCachedAuthorizationHeader2 = createAuthorizationHeader(refreshToken2);
        syncWithRetrofit();

        mLastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public boolean isSigned() {
        // Condition created for the case when a device in offline mode.
        return mAccountManager.getSelectedAccount() != null;
    }

    @Override
    public Observable<Boolean> isSignedObserve() {
        return RxHelper.fromCallable(this::isSigned);
    }

    @Override
    public List<Account> getAccounts() {
        return mAccountManager.getAccounts();
    }

    @Override
    public Observable<List<Account>> getAccountsObserve() {
        return RxHelper.fromCallable(this::getAccounts);
    }

    @Nullable
    @Override
    public Account getSelectedAccount() {
        return mAccountManager.getSelectedAccount();
    }

    /**
     * For testing purposes
     */
    public void setAuthorizationHeader(String authorizationHeader) {
        mCachedAuthorizationHeader = authorizationHeader;
        mCachedAuthorizationHeader2 = null;
        mLastUpdateTime = System.currentTimeMillis();

        syncWithRetrofit();
    }

    public void invalidateCache() {
        mCachedAuthorizationHeader = null;
    }

    @Override
    public void selectAccount(Account account) {
        mAccountManager.selectAccount(account);
    }

    @Override
    public void removeAccount(Account account) {
        mAccountManager.removeAccount(account);
    }

    /**
     * Authorization should be updated periodically (see expire_in field in response)
     */
    private synchronized String createAuthorizationHeader(String refreshToken) {
        Log.d(TAG, "Updating authorization header...");

        String authorizationHeader = null;

        AccessToken token = obtainAccessToken(refreshToken);

        if (token != null) {
            authorizationHeader = String.format("%s %s", token.getTokenType(), token.getAccessToken());
        } else {
            Log.e(TAG, "Access token is null!");
        }

        return authorizationHeader;
    }

    private AccessToken obtainAccessToken(String refreshToken) {
        // We don't have context, so can't create instance here.
        // Let's hope someone already created one for us.
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "GlobalPreferences is null!");
            return null;
        }

        AccessToken token = null;

        if (refreshToken != null) {
            token = mAuthService.getAccessToken(refreshToken);
        } else {
            String rawAuthData = GlobalPreferences.sInstance.getRawAuthData();

            if (rawAuthData != null) {
                token = mAuthService.getAccessTokenRaw(rawAuthData);
            } else {
                Log.e(TAG, "Refresh token data doesn't stored in the app registry!");
            }
        }

        return token;
    }

    private void syncWithRetrofit() {
        Map<String, String> headers = RetrofitOkHttpHelper.getAuthHeaders();
        Map<String, String> headers2 = RetrofitOkHttpHelper.getAuthHeaders2();
        headers.clear();
        headers2.clear();

        if (mCachedAuthorizationHeader != null && getSelectedAccount() != null) {
            headers.put("Authorization", mCachedAuthorizationHeader);
            String pageIdToken = ((YouTubeAccount) getSelectedAccount()).getPageIdToken();
            if (pageIdToken != null) {
                headers2.put("Authorization", mCachedAuthorizationHeader2);
                // Apply branded account rights (restricted videos). Branded refresh token with current account page id.
                headers.put("X-Goog-Pageid", pageIdToken);
            }
        }
    }

    @Override
    public void setOnChange(Runnable onChange) {
        mAccountManager.setOnChange(onChange);
    }
}
