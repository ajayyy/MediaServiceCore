package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.browse.v1.BrowseApiHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.next.v2.impl.MediaItemMetadataImpl
import com.liskovsoft.youtubeapi.next.v2.impl.mediagroup.MediaGroupImpl
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResultContinuation
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.next.v2.gen.DislikesResult

class WatchNextService private constructor() {
    private var mWatchNextApi = RetrofitHelper.withGson(WatchNextApi::class.java)
    private val mAppService = AppService.instance();

    fun getMetadata(videoId: String): MediaItemMetadata? {
        return getMetadata(videoId, null, 0)
    }

    fun getMetadata(item: MediaItem): MediaItemMetadata? {
        return getMetadata(item.videoId, item.playlistId, item.playlistIndex)
    }

    fun getMetadata(videoId: String?, playlistId: String?, playlistIndex: Int): MediaItemMetadata? {
        return getMetadata(videoId, playlistId, playlistIndex, null)
    }

    fun getMetadata(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): MediaItemMetadata? {
        val watchNextResult = getWatchNextResult(videoId, playlistId, playlistIndex, playlistParams)

        return if (watchNextResult != null) MediaItemMetadataImpl(watchNextResult, getDislikesResult(videoId)) else null
    }

    fun continueGroup(mediaGroup: MediaGroup?): MediaGroup? {
        val nextKey = YouTubeHelper.extractNextKey(mediaGroup)

        if (nextKey == null) {
            return null;
        }

        val continuation = continueWatchNext(BrowseApiHelper.getContinuationQuery(nextKey))

        return MediaGroupImpl.from(continuation, mediaGroup)
    }

    private fun getWatchNextResult(videoId: String?): WatchNextResult? {
        return getWatchNext(WatchNextApiHelper.getWatchNextQuery(videoId!!))
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int): WatchNextResult? {
        return getWatchNext(WatchNextApiHelper.getWatchNextQuery(videoId, playlistId, playlistIndex))
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): WatchNextResult? {
        return getWatchNext(WatchNextApiHelper.getWatchNextQuery(videoId, playlistId, playlistIndex, playlistParams))
    }

    private fun getWatchNext(query: String): WatchNextResult? {
        val wrapper = mWatchNextApi.getWatchNextResult(query, mAppService.visitorId)

        return RetrofitHelper.get(wrapper)
    }

    private fun continueWatchNext(query: String): WatchNextResultContinuation? {
        val wrapper = mWatchNextApi.continueWatchNextResult(query, mAppService.visitorId)

        return RetrofitHelper.get(wrapper)
    }

    private fun getDislikesResult(videoId: String?): DislikesResult? {
        if (videoId == null) {
            return null
        }

        val wrapper = mWatchNextApi.getDislikes(videoId)

        return RetrofitHelper.get(wrapper)
    }

    /**
     * For testing (mocking) purposes only
     */
    fun setWatchNextApi(watchNextApi: WatchNextApi) {
        mWatchNextApi = watchNextApi
    }

    companion object {
        private var sInstance: WatchNextService? = null
        @JvmStatic
        fun instance(): WatchNextService? {
            if (sInstance == null) {
                sInstance = WatchNextService()
            }
            return sInstance
        }

        @JvmStatic
        fun unhold() {
            sInstance = null
        }
    }
}