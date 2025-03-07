package com.liskovsoft.youtubeapi.dearrow

import com.liskovsoft.youtubeapi.dearrow.data.BrandingList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface DeArrowApi {
    @GET(DeArrowApiHelper.BRANDING_URL)
    fun getBranding(@Query("videoID") videoId: String?): Call<BrandingList?>?

    @GET(DeArrowApiHelper.THUMBNAIL_URL)
    fun getThumbnail(@Query("videoID") videoId: String?): Call<ResponseBody?>?
}