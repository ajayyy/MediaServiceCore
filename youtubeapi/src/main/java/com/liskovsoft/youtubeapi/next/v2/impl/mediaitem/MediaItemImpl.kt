package com.liskovsoft.youtubeapi.next.v2.impl.mediaitem

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.next.v2.helpers.*
import com.liskovsoft.youtubeapi.next.v2.gen.kt.ItemWrapper
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

data class MediaItemImpl(var itemWrapper: ItemWrapper): BaseMediaItemImpl() {
    override val typeItem by lazy { itemWrapper.getType() }
    override val videoIdItem by lazy { itemWrapper.getVideoId() }
    override val titleItem by lazy { itemWrapper.getTitle() }
    override val descBadgeText by lazy { itemWrapper.getDescBadgeText() }
    override val userName by lazy { itemWrapper.getUserName() }
    override val publishedTime by lazy { itemWrapper.getPublishedTime() }
    override val viewCountText by lazy { itemWrapper.getViewCountText() }
    override val upcomingEventText by lazy { itemWrapper.getUpcomingEventText() }
    override val descriptionItem by lazy { YouTubeMediaServiceHelper.createDescription(descBadgeText, userName, viewCountText, publishedTime, upcomingEventText) ?: null }
    override val cardThumbImageUrl by lazy { itemWrapper.getThumbnails()?.findHighResThumbnailUrl() }
    override val playlistIdItem by lazy { itemWrapper.getPlaylistId() }
    override val playlistIndexItem by lazy { itemWrapper.getPlaylistIndex() }
    override val badgeTextItem by lazy { itemWrapper.getBadgeText() }
    override val durationItemMs by lazy { ServiceHelper.timeTextToMillis(itemWrapper.getLengthText()) }
    override val channelIdItem by lazy { itemWrapper.getChannelId() }
}
