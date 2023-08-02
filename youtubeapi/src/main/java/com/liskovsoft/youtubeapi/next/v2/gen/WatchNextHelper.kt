package com.liskovsoft.youtubeapi.next.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.*

//////

fun VideoOwnerItem.isSubscribed() = subscriptionButton?.subscribed ?: subscribed ?: subscribeButton?.subscribeButtonRenderer?.subscribed ?:
    navigationEndpoint?.getOverlayToggleButton()?.isToggled ?: navigationEndpoint?.getOverlaySubscribeButton()?.subscribed
fun VideoOwnerItem.getChannelId() = navigationEndpoint?.getBrowseId() ?: subscribeButton?.subscribeButtonRenderer?.channelId
fun VideoOwnerItem.getThumbnails() = thumbnail
fun VideoOwnerItem.getParams() = navigationEndpoint?.getOverlayToggleButton()?.getSubscribeParams() ?: navigationEndpoint?.getOverlaySubscribeButton()?.getParams()

/////

private fun WatchNextResult.getWatchNextResults() = contents?.singleColumnWatchNextResults
private fun WatchNextResult.getPlayerOverlays() = playerOverlays?.playerOverlayRenderer
fun WatchNextResult.getSuggestedSections() = getWatchNextResults()?.pivot?.let { it.pivot ?: it.sectionListRenderer }?.contents?.mapNotNull { it?.shelfRenderer }
fun WatchNextResult.getVideoMetadata() = getWatchNextResults()?.results?.results?.contents?.getOrNull(0)?.
    itemSectionRenderer?.contents?.map { it?.videoMetadataRenderer ?: it?.musicWatchMetadataRenderer }?.firstOrNull()

fun WatchNextResult.getNextVideoItem() = getWatchNextResults()?.autoplay?.autoplay?.sets?.getOrNull(0)?.
    nextVideoRenderer?.let { it.maybeHistoryEndpointRenderer ?: it.autoplayEndpointRenderer ?: it.autoplayVideoWrapperRenderer?.primaryEndpointRenderer?.autoplayEndpointRenderer }

fun WatchNextResult.getVideoDetails() = getReplayItemWrapper()?.pivotVideoRenderer
fun WatchNextResult.getReplayItemWrapper() = getWatchNextResults()?.autoplay?.autoplay?.replayVideoRenderer
fun WatchNextResult.getButtonStateItem() = transportControls?.transportControlsRenderer
fun WatchNextResult.getLiveChatKey() = getWatchNextResults()?.conversationBar?.liveChatRenderer?.continuations?.getOrNull(0)?.reloadContinuationData?.continuation
fun WatchNextResult.getPlaylistInfo() = getWatchNextResults()?.playlist?.playlist
fun WatchNextResult.getChapters() = getPlayerOverlays()?.decoratedPlayerBarRenderer?.decoratedPlayerBarRenderer?.
    playerBar?.multiMarkersPlayerBarRenderer?.markersMap?.firstOrNull()?.value?.chapters
fun WatchNextResult.getCommentPanel() = engagementPanels?.firstOrNull { it?.isCommentsSection() == true }
fun WatchNextResult.isEmpty(): Boolean = getSuggestedSections()?.firstOrNull()?.let { it.getNextPageKey() == null || it.getItemWrappers()?.size ?: 0 <= 3 } ?: true

fun WatchNextResultContinuation.isEmpty(): Boolean = continuationContents?.horizontalListContinuation?.items == null

///////

const val LIKE_STATUS_LIKE = "LIKE"
const val LIKE_STATUS_DISLIKE = "DISLIKE"
const val LIKE_STATUS_INDIFFERENT = "INDIFFERENT"
fun VideoMetadataItem.getVideoOwner() = owner?.videoOwnerRenderer
fun VideoMetadataItem.getTitle() = title?.getText()
fun VideoMetadataItem.getViewCountText() = viewCount?.videoViewCountRenderer?.viewCount?.getText() ?: viewCountText?.getText()
fun VideoMetadataItem.isLive() = viewCount?.videoViewCountRenderer?.isLive
fun VideoMetadataItem.getDateText() = dateText?.getText()
fun VideoMetadataItem.getPublishedTime() = publishedTimeText?.getText() ?: publishedTime?.getText() ?: albumName?.getText()
fun VideoMetadataItem.getLikeStatus() = likeStatus ?: likeButton?.likeButtonRenderer?.likeStatus
fun VideoMetadataItem.getLikeCount() = likeStatus ?: likeButton?.likeButtonRenderer?.likeCountText?.getText()
fun VideoMetadataItem.isUpcoming() = badges?.firstNotNullOfOrNull { it?.upcomingEventBadge?.label?.getText() }?.let { true } ?: false
fun VideoMetadataItem.getPercentWatched() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayResumePlaybackRenderer?.percentDurationWatched } ?: 0

////////

const val TYPE_CHANNEL = "TRANSPORT_CONTROLS_BUTTON_TYPE_CHANNEL_BUTTON"
const val TYPE_SKIP_PREVIOUS = "TRANSPORT_CONTROLS_BUTTON_TYPE_SKIP_PREVIOUS"
const val TYPE_SKIP_NEXT = "TRANSPORT_CONTROLS_BUTTON_TYPE_SKIP_NEXT"
const val TYPE_LIKE = "TRANSPORT_CONTROLS_BUTTON_TYPE_LIKE_BUTTON"
const val TYPE_DISLIKE = "TRANSPORT_CONTROLS_BUTTON_TYPE_DISLIKE_BUTTON"
const val TYPE_ADD_TO_PLAYLIST = "TRANSPORT_CONTROLS_BUTTON_TYPE_ADD_TO_PLAYLIST"

fun ButtonStateItem.isLikeToggled() = likeButton?.toggleButtonRenderer?.isToggled ?: getButton(TYPE_LIKE)?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.isDislikeToggled() = dislikeButton?.toggleButtonRenderer?.isToggled ?: getButton(TYPE_DISLIKE)?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.isSubscribeToggled() = subscribeButton?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.getChannelId() = getChannelOwner()?.getChannelId()
fun ButtonStateItem.getChannelOwner() = channelButton?.videoOwnerRenderer ?: getButton(TYPE_CHANNEL)?.videoOwnerRenderer
private fun ButtonStateItem.getButton(type: String) = buttons?.firstOrNull { it?.type == type }?.button

///////

fun ShelfItem.getTitle() = title?.getText() ?: getShelf()?.title?.getText() ?: getShelf()?.avatarLockup?.avatarLockupRenderer?.title?.getText()
fun ShelfItem.getItemWrappers() = content?.horizontalListRenderer?.items
fun ShelfItem.getNextPageKey() = content?.horizontalListRenderer?.continuations?.firstNotNullOfOrNull { it?.nextContinuationData?.continuation }
fun ShelfItem.getChipItems() = headerRenderer?.chipCloudRenderer?.chips
private fun ShelfItem.getShelf() = headerRenderer?.shelfHeaderRenderer

////////

/**
 * In some cases chip item contains multiple shelfs<br/>
 * Other regular shelfs in this case is empty
 */
fun ChipItem.getShelfItems() = chipCloudChipRenderer?.content?.sectionListRenderer?.contents?.map { it?.shelfRenderer }
fun ChipItem.getTitle() = chipCloudChipRenderer?.text?.getText()

//////

fun NextVideoItem.getVideoId() = endpoint?.watchEndpoint?.videoId
fun NextVideoItem.getTitle() = item?.previewButtonRenderer?.title?.getText()
fun NextVideoItem.getAuthor() = item?.previewButtonRenderer?.byline?.getText()
fun NextVideoItem.getThumbnails() = item?.previewButtonRenderer?.thumbnail
fun NextVideoItem.getPlaylistId() = endpoint?.watchEndpoint?.playlistId
fun NextVideoItem.getPlaylistIndex() = endpoint?.watchEndpoint?.index
fun NextVideoItem.getParams() = endpoint?.watchEndpoint?.params

///////

fun ChapterItem.getTitle() = chapterRenderer?.title?.toString()
fun ChapterItem.getStartTimeMs() = chapterRenderer?.timeRangeStartMillis
fun ChapterItem.getThumbnailUrl() = chapterRenderer?.thumbnail?.getOptimalResThumbnailUrl()

///////

fun ContinuationItem.getKey(): String? = nextContinuationData?.continuation ?: reloadContinuationData?.continuation
fun ContinuationItem.getLabel(): String? = nextContinuationData?.label?.getText()

///////

fun EngagementPanel.getMenu() = engagementPanelSectionListRenderer?.header?.engagementPanelTitleHeaderRenderer?.menu
fun EngagementPanel.getTopCommentsKey(): String? = getMenu()?.getSubMenuItems()?.getOrNull(0)?.continuation?.getKey()
fun EngagementPanel.getNewCommentsKey(): String? = getMenu()?.getSubMenuItems()?.getOrNull(1)?.continuation?.getKey()
fun EngagementPanel.isCommentsSection(): Boolean = engagementPanelSectionListRenderer?.panelIdentifier == "comment-item-section"
fun EngagementPanel.getTitle(): String? = getVideoDescription()?.title?.getText()
fun EngagementPanel.getChannelName(): String? = getVideoDescription()?.channel?.getText()
fun EngagementPanel.getViews(): String? = getVideoDescription()?.views?.getText()
fun EngagementPanel.getPublishDate(): String? = getVideoDescription()?.publishDate?.getText()
fun EngagementPanel.getBrowseId(): String? = getVideoDescription()?.channelNavigationEndpoint?.getBrowseId()
private fun EngagementPanel.getVideoDescription(): VideoDescriptionHeaderRenderer? =
    engagementPanelSectionListRenderer?.content?.structuredDescriptionContentRenderer?.items?.firstNotNullOfOrNull { it?.videoDescriptionHeaderRenderer }
fun Menu.getSubMenuItems() = sortFilterSubMenuRenderer?.subMenuItems

