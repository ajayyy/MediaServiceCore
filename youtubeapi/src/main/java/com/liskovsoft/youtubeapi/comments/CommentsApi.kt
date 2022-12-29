package com.liskovsoft.youtubeapi.comments

import com.liskovsoft.youtubeapi.comments.gen.kt.CommentsResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CommentsApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    fun getComments(@Body commentsQuery: String?): Call<CommentsResult?>?
}