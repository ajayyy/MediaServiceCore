package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

/**
 * Data contained withing m=base js file
 */
public class ModernClientData implements ClientData {
    @RegExp("\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",[\\w]{2}=\"\\w+\"")
    private String mClientId;

    @RegExp("\"[-\\w]+\\.apps\\.googleusercontent\\.com\",[\\w]{2}=\"(\\w+)\"")
    private String mClientSecret;

    @Override
    public String getClientId() {
        return mClientId;
    }

    @Override
    public String getClientSecret() {
        return mClientSecret;
    }
}
