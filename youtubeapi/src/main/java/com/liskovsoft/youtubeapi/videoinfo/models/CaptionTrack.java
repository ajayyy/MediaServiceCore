package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.sharedutils.querystringparser.UrlQueryString;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryStringFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.V2.TextItem;

public class CaptionTrack {
    private static final String MIME_TYPE_VTT = "text/vtt";
    /**
     * YouTube subs not supported by ExoPlayer?<br/>
     * https://js-jrod.medium.com/the-first-complete-guide-to-youtube-captions-f886e06f7d9d<br/>
     * https://en.wikipedia.org/wiki/Timed_Text_Markup_Language<br/>
     */
    private static final String MIME_TYPE_SRV3 = "application/ttml+xml";
    private static final String PARAM_VTT = "vtt";
    private static final String PARAM_SRV3 = "srv3";
    private static final String CODECS_VTT = "wvtt";
    private static final String CODECS_SRV3 = "ttml";
    private static final String TYPE_ASR = "asr";

    /**
     * Example: "https://www.youtube.com/api/timedtext?caps=&key=yt…&sparams=caps%2Cv%2Cxorp%2Cexpire&lang=en&name=en"
     */
    @JsonPath("$.baseUrl")
    private String mBaseUrl;
    /**
     * Example: true
     */
    @JsonPath("$.isTranslatable")
    private boolean mIsTranslatable;
    /**
     * Example: "en"
     */
    @JsonPath("$.languageCode")
    private String mLanguageCode;
    /**
     * Example: ".en.nP7-2PuUl7o"
     */
    @JsonPath("$.vssId")
    private String mVssId;
    @JsonPath("$.name")
    private TextItem mName;
    /**
     * E.g. asr (Automatic Speech Recognition)
     */
    @JsonPath("$.kind")
    private String mType;
    private String mMimeType = MIME_TYPE_VTT;
    private String mCodecs = CODECS_VTT;
    private UrlQueryString mBaseUrlQuery;

    public String getBaseUrl() {
        UrlQueryString baseUrlQuery = getBaseUrlQuery();

        if (baseUrlQuery == null) {
            return null;
        }

        baseUrlQuery.set("fmt", PARAM_VTT);
        
        return baseUrlQuery.toString();
    }

    public boolean isTranslatable() {
        return mIsTranslatable;
    }

    public String getLanguageCode() {
        return mLanguageCode;
    }

    public String getVssId() {
        return mVssId;
    }

    public String getName() {
        return mName != null ? mName.getText() : null;
    }

    public String getType() {
        return mType;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public String getCodecs() {
        return mCodecs;
    }

    public boolean isAutogenerated() {
        return TYPE_ASR.equals(mType);
    }

    private UrlQueryString getBaseUrlQuery() {
        if (mBaseUrl == null) {
            return null;
        }

        if (mBaseUrlQuery == null) {
            mBaseUrlQuery = UrlQueryStringFactory.parse(mBaseUrl);
        }

        return mBaseUrlQuery;
    }
}
