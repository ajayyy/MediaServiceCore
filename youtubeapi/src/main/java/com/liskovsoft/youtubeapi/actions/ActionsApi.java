package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ActionsApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/like/like")
    Call<ActionResult> setLike(@Body String actionQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/like/removelike")
    Call<ActionResult> removeLike(@Body String actionQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/like/dislike")
    Call<ActionResult> setDislike(@Body String actionQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/like/removedislike")
    Call<ActionResult> removeDislike(@Body String actionQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/subscription/subscribe")
    Call<ActionResult> subscribe(@Body String actionQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/subscription/unsubscribe")
    Call<ActionResult> unsubscribe(@Body String actionQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/history/pause_watch_history")
    Call<Void> pauseWatchHistory(@Body String historyQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/history/resume_watch_history")
    Call<Void> resumeWatchHistory(@Body String historyQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/history/clear_watch_history")
    Call<Void> clearWatchHistory(@Body String historyQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/history/clear_search_history")
    Call<Void> clearSearchHistory(@Body String historyQuery, @Header("Authorization") String auth);
}
