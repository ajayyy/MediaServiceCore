package com.liskovsoft.youtubeapi.next.v2.mock

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.next.v2.WatchNextManagerParams
import com.liskovsoft.youtubeapi.next.v2.gen.kt.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.impl.MediaItemMetadataImpl
import com.liskovsoft.youtubeapi.service.YouTubeSignInManager

class WatchNextServiceV2Mock private constructor() {
    private val mWatchNextManager = MockUtils.mockWithGson(WatchNextManagerMock::class.java)
    private val mSignInManager = YouTubeSignInManager.instance()

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

        return if (watchNextResult != null) MediaItemMetadataImpl(watchNextResult) else null
    }

    //fun continueGroup(mediaGroup: MediaGroup?): MediaGroup? {
    //    val nextKey = YouTubeMediaServiceHelper.extractNextKey(mediaGroup)
    //
    //    val continuation = continueWatchNext(BrowseManagerParams.getContinuationQuery(nextKey))
    //
    //    return MediaGroupImpl.from(continuation, mediaGroup)
    //}

    private fun getWatchNextResult(videoId: String?): WatchNextResult? {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId!!))
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int): WatchNextResult? {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId, playlistId, playlistIndex))
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): WatchNextResult? {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId, playlistId, playlistIndex, playlistParams))
    }

    private fun getWatchNext(query: String): WatchNextResult? {
        val wrapper = mWatchNextManager.getWatchNextResultUnsigned(query)

        return RetrofitHelper.get(wrapper)
    }

    //private fun continueWatchNext(query: String): WatchNextResultContinuation? {
    //    val wrapper = if (mSignInManager.checkAuthHeader())
    //        mWatchNextManager.continueWatchNextResultSigned(query, mSignInManager.authorizationHeader)
    //    else
    //        mWatchNextManager.continueWatchNextResultUnsigned(query)
    //    return RetrofitHelper.get(wrapper)
    //}

    companion object {
        private var sInstance: WatchNextServiceV2Mock? = null
        fun instance(): WatchNextServiceV2Mock? {
            if (sInstance == null) {
                sInstance = WatchNextServiceV2Mock()
            }
            return sInstance
        }

        fun unhold() {
            sInstance = null
        }
    }
}