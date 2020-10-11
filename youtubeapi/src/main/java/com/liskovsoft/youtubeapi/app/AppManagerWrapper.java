package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.BaseData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

public class AppManagerWrapper {
    private final AppManager mAppManager;

    public AppManagerWrapper() {
        mAppManager = RetrofitHelper.withRegExp(AppManager.class);
    }
    
    public AppInfo getAppInfo(String userAgent) {
        Call<AppInfo> wrapper = mAppManager.getAppInfo(userAgent);
        return RetrofitHelper.get(wrapper);
    }
    
    public PlayerData getPlayerData(String playerUrl) {
        Call<PlayerData> wrapper = mAppManager.getPlayerData(playerUrl);
        return RetrofitHelper.get(wrapper);
    }
    
    public BaseData getBaseData(String baseUrl) {
        Call<BaseData> wrapper = mAppManager.getBaseData(baseUrl);
        BaseData baseData = RetrofitHelper.get(wrapper);

        // Seem that lacy script encountered.
        // Needed values is stored in main script, not in base.
        if (baseData == null) {
            baseData = RetrofitHelper.get(mAppManager.getBaseData(getMainUrl(baseUrl)));
        }

        return baseData;
    }

    /**
     * Converts base script url to main script url
     */
    private static String getMainUrl(String baseUrl) {
        if (baseUrl == null) {
            return null;
        }

        return baseUrl
                .replace("/dg=0/", "/exm=base/ed=1/")
                .replace("/m=base", "/m=main");
    }
}
