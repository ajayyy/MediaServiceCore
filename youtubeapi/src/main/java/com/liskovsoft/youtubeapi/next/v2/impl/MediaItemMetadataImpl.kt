package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.*
import com.liskovsoft.mediaserviceinterfaces.data.ChapterItem
import com.liskovsoft.mediaserviceinterfaces.data.NotificationState
import com.liskovsoft.mediaserviceinterfaces.data.PlaylistInfo
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.common.models.gen.*
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResult
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.SuggestionsGroup
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.NextMediaItem
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.common.models.impl.NotificationStateImpl
import com.liskovsoft.youtubeapi.next.v2.gen.*

data class MediaItemMetadataImpl(val watchNextResult: WatchNextResult,
                                 val dislikesResult: DislikesResult? = null,
                                 val suggestionsResult: WatchNextResult? = null) : MediaItemMetadata {
    private val channelIdItem by lazy {
        videoDetails?.getChannelId() ?: videoOwner?.getChannelId() ?: channelOwner?.getChannelId()
    }
    private val percentWatchedItem by lazy {
        videoMetadata?.getPercentWatched() ?: 0
    }
    private val suggestedSections by lazy {
        (suggestionsResult ?: watchNextResult).getSuggestedSections()
    }
    private val videoMetadata by lazy {
        watchNextResult.getVideoMetadata()
    }
    private val nextVideoItem by lazy {
        watchNextResult.getNextVideoItem()
    }
    private val commentsPanel by lazy {
        watchNextResult.getCommentPanel()
    }
    private val liveChatKeyItem by lazy {
        watchNextResult.getLiveChatKey()
    }
    private val commentsKeyItem: String? by lazy {
        commentsPanel?.getTopCommentsKey()
        // Old val
        //suggestedSections?.lastOrNull()?.getItemWrappers()?.getOrNull(1)?.getContinuationKey()
    }
    private val videoOwner by lazy {
        videoMetadata?.getVideoOwner()
    }
    private val channelOwner by lazy {
        watchNextResult.transportControls?.transportControlsRenderer?.getChannelOwner()
    }
    private val notificationPreference by lazy {
        videoOwner?.getNotificationPreference()
    }
    private val videoDetails by lazy {
        watchNextResult.getVideoDetails()
    }
    private val nextMediaItem by lazy {
        nextVideoItem?.let { NextMediaItem(it) }
    }
    private val isSubscribedItem by lazy {
        videoOwner?.isSubscribed() ?: channelOwner?.isSubscribed() ?: false
    }
    private val paramsItem by lazy {
        videoOwner?.getParams() ?: channelOwner?.getParams()
    }
    private val replayItemWrapper by lazy {
        watchNextResult.getReplayItemWrapper()
    }
    private val buttonStateItem by lazy {
        watchNextResult.getButtonStateItem()
    }
    private val videoTitle by lazy {
        videoDetails?.getTitle() ?: videoMetadata?.getTitle()
    }
    private val isUpcomingItem by lazy {
        videoMetadata?.isUpcoming() ?: false
    }
    private val likeStatusItem by lazy {
        when (videoMetadata?.getLikeStatus()) {
            LIKE_STATUS_LIKE -> MediaItemMetadata.LIKE_STATUS_LIKE
            LIKE_STATUS_DISLIKE -> MediaItemMetadata.LIKE_STATUS_DISLIKE
            LIKE_STATUS_INDIFFERENT -> MediaItemMetadata.LIKE_STATUS_INDIFFERENT
            else -> {
                when {
                    watchNextResult.transportControls?.transportControlsRenderer?.isLikeToggled() == true -> MediaItemMetadata.LIKE_STATUS_LIKE
                    watchNextResult.transportControls?.transportControlsRenderer?.isDislikeToggled() == true -> MediaItemMetadata.LIKE_STATUS_DISLIKE
                    else -> MediaItemMetadata.LIKE_STATUS_INDIFFERENT
                }
            }
        }
    }
    private val videoDescription by lazy { videoMetadata?.description?.getText() ?:
        // Scroll to the end till we find description tile
        suggestionList?.lastOrNull()?.shelf?.getItemWrappers()?.firstOrNull()?.getDescriptionText()
    }
    private val videoSecondTitle by lazy {
        YouTubeHelper.createInfo(
                videoAuthor, viewCountText, publishedTime
        )
    }
    private val videoSecondTitleAlt by lazy {
        YouTubeHelper.createInfo(
                videoAuthor, viewCountText, publishedDate
        )
    }
    private val videoAuthor by lazy { videoDetails?.getUserName() }
    private val videoAuthorImageUrl by lazy { (videoOwner?.getThumbnails() ?: channelOwner?.getThumbnails())?.getOptimalResThumbnailUrl() }
    private val suggestionList by lazy {
        val list = suggestedSections?.mapNotNull { if (it?.getItemWrappers() != null) SuggestionsGroup(it) else null }
        if (list?.size ?: 0 > 0)
            list
        else
            // In rare cases first chip item contains all shelfs
            suggestedSections?.firstOrNull()?.getChipItems()?.firstOrNull()?.run {
                val chipTitle = getTitle() // shelfs inside a chip aren't have a titles
                getShelfItems()?.map { it?.let { SuggestionsGroup(it).apply { title = title ?: chipTitle } } }
            }
    }

    private val viewCountText by lazy {
        videoMetadata?.getViewCountText()
    }

    private val publishedTime by lazy {
        videoMetadata?.getPublishedTime()
    }

    private val publishedDateText by lazy {
        videoMetadata?.getDateText() ?: videoDetails?.getPublishedTimeText()
    }

    private val videoIdItem by lazy {
        videoMetadata?.videoId ?: videoDetails?.videoId
    }

    private val isLiveStream by lazy {
        videoMetadata?.isLive() ?: liveChatKeyItem != null
    }

    private val playlistInfoItem by lazy {
        watchNextResult.getPlaylistInfo()?.let {
            object: PlaylistInfo {
                override fun getTitle() = it.title
                override fun getPlaylistId() = it.playlistId
                override fun isSelected() = false
                override fun getSize() = it.totalVideos ?: -1
                override fun getCurrentIndex() = it.currentIndex ?: -1
            }
        }
    }

    private val chapterList by lazy {
        watchNextResult.getChapters()?.map {
            object: ChapterItem {
                override fun getTitle() = it?.getTitle()
                override fun getStartTimeMs() = it?.getStartTimeMs() ?: -1
                override fun getCardImageUrl() = it?.getThumbnailUrl()
            }
        }
    }

    private val notificationStateList by lazy {
        val currentId = notificationPreference?.getCurrentStateId()
        val result = notificationPreference?.getItems()?.mapNotNull {
            it?.let { NotificationStateImpl(it, currentId) }
        }

        result?.forEach { it.allStates = result }

        result
    }

    private val likeCountItem by lazy {
        dislikesResult?.getLikeCount()?.let { "$it ${Helpers.THUMB_UP}" }
    }

    private val dislikeCountItem by lazy {
        dislikesResult?.getDislikeCount()?.let { "$it ${Helpers.THUMB_DOWN}" }
    }

    override fun getTitle(): String? {
        return videoTitle
    }

    override fun getSecondTitle(): String? {
        return videoSecondTitle
    }

    override fun getSecondTitleAlt(): String? {
        return videoSecondTitleAlt
    }

    override fun getDescription(): String? {
        return videoDescription;
    }

    override fun getAuthor(): String? {
        return videoAuthor
    }

    override fun getAuthorImageUrl(): String? {
        return videoAuthorImageUrl
    }

    override fun getViewCount(): String? {
        return viewCountText
    }

    override fun getPublishedDate(): String? {
        return publishedDateText
    }

    override fun getVideoId(): String? {
        return videoIdItem
    }

    override fun getNextVideo(): MediaItem? {
        return nextMediaItem
    }

    override fun isSubscribed(): Boolean {
        return isSubscribedItem
    }

    override fun getParams(): String? {
        return paramsItem
    }

    override fun isLive(): Boolean {
        return isLiveStream
    }

    override fun getLiveChatKey(): String? {
        return liveChatKeyItem
    }

    override fun getCommentsKey(): String? {
        return commentsKeyItem
    }

    override fun isUpcoming(): Boolean {
        return isUpcomingItem
    }

    override fun getChannelId(): String? {
        return channelIdItem
    }

    override fun getPercentWatched(): Int {
        return percentWatchedItem
    }

    override fun getLikeStatus(): Int {
        return likeStatusItem
    }

    override fun getLikeCount(): String? {
        return likeCountItem
    }

    override fun getDislikeCount(): String? {
        return dislikeCountItem
    }

    override fun getSuggestions(): List<MediaGroup?>? {
        return suggestionList
    }

    override fun getPlaylistInfo(): PlaylistInfo? {
        return playlistInfoItem
    }

    override fun getChapters(): List<ChapterItem>? {
        return chapterList
    }

    override fun getNotificationStates(): List<NotificationState?>? {
        return notificationStateList
    }
}
