package com.liskovsoft.mediaserviceinterfaces.data;

public interface SearchOptions {
    int UPLOAD_DATE_LAST_HOUR = 0b1;
    int UPLOAD_DATE_TODAY = 0b10;
    int UPLOAD_DATE_THIS_WEEK = 0b100;
    int UPLOAD_DATE_THIS_MONTH = 0b1000;
    int UPLOAD_DATE_THIS_YEAR = 0b10000;

    int DURATION_UNDER_4 = 0b100000;
    int DURATION_BETWEEN_4_20 = 0b1000000;
    int DURATION_OVER_20 = 0b10000000;

    int TYPE_VIDEO = 0b100000000;
    int TYPE_CHANNEL = 0b1000000000;
    int TYPE_PLAYLIST = 0b10000000000;
    int TYPE_MOVIE = 0b100000000000;

    int FEATURE_LIVE = 0b1000000000000;
    int FEATURE_4K = 0b10000000000000;
    int FEATURE_HDR = 0b100000000000000;
    
    //int FEATURES_4K = 0b100000;
    //int SORT_BY_UPLOAD_DATE = 0b1000000;
}
