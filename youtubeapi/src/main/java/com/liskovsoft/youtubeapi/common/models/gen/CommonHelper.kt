package com.liskovsoft.youtubeapi.common.models.gen

import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.next.v2.gen.getKey

private const val TILE_CONTENT_TYPE_UNDEFINED = "UNDEFINED"
private const val TILE_CONTENT_TYPE_CHANNEL = "TILE_CONTENT_TYPE_CHANNEL"
private const val TILE_CONTENT_TYPE_PLAYLIST = "TILE_CONTENT_TYPE_PLAYLIST"
private const val TILE_CONTENT_TYPE_VIDEO = "TILE_CONTENT_TYPE_VIDEO"
private const val BADGE_STYLE_LIVE = "LIVE"
private const val BADGE_STYLE_UPCOMING = "UPCOMING"
private const val BADGE_STYLE_SHORTS = "SHORTS"
private const val BADGE_STYLE_DEFAULT = "DEFAULT"
private const val BADGE_STYLE_MOVIE = "BADGE_STYLE_TYPE_YPC"
private const val OLD_BADGE_STYLE_LIVE = "BADGE_STYLE_TYPE_LIVE_NOW"
private const val ICON_TYPE_NOT_INTERESTED = "NOT_INTERESTED"
private const val ICON_TYPE_REMOVE = "REMOVE"

///////////

fun TextItem.getText() = runs?.joinToString("") { it?.text ?: it?.emoji?.getText() ?: "" } ?: simpleText
fun TextItem.getAccessibilityLabel() = accessibility?.accessibilityData?.label

/**
 * Use shortcut name as workaround to display custom emoji. Custom emoji are images.
 */
//fun LiveChatEmoji.getText() = if (isCustomEmoji == true) shortcuts?.getOrElse(0) { "" } else emojiId
/**
 * Use empty string as workaround to display custom emoji. Custom emoji are images.
 */
fun LiveChatEmoji.getText() = if (isCustomEmoji == true) "" else emojiId

/**
 * Find optimal thumbnail for tv screen
 */
fun ThumbnailItem.getOptimalResThumbnailUrl() = thumbnails?.getOrElse(YouTubeHelper.OPTIMAL_RES_THUMBNAIL_INDEX) { thumbnails.lastOrNull() } ?.getUrl()
fun ThumbnailItem.getHighResThumbnailUrl() = thumbnails?.lastOrNull()?.getUrl()
fun ThumbnailItem.Thumbnail.getUrl(): String? {
    var newUrl = if (url?.startsWith("//") == true) "https:$url" else url

    newUrl = YouTubeHelper.avatarBlockFix(newUrl)

    return newUrl
}

////////

fun NavigationEndpointItem.getBrowseId() = browseEndpoint?.browseId
fun NavigationEndpointItem.getBrowseParams() = browseEndpoint?.params
fun NavigationEndpointItem.getOverlayToggleButton() = getContent()?.overlayPanelItemListRenderer?.items?.firstNotNullOfOrNull { it?.toggleButtonRenderer }
fun NavigationEndpointItem.getOverlaySubscribeButton() = getContent()?.overlayPanelItemListRenderer?.items?.firstNotNullOfOrNull { it?.subscribeButtonRenderer }
fun NavigationEndpointItem.isSubscribed() = getOverlaySubscribeButton()?.subscribed
fun NavigationEndpointItem.getContinuation() = getContent()?.itemSectionRenderer?.continuations?.getOrNull(0)
fun NavigationEndpointItem.getTitle() = getHeader()?.overlayPanelHeaderRenderer?.title?.getText()
fun NavigationEndpointItem.getSubtitle() = getHeader()?.overlayPanelHeaderRenderer?.subtitle?.getText()
private fun NavigationEndpointItem.getOverlayPanel() = openPopupAction?.popup?.overlaySectionRenderer?.overlay
    ?.overlayTwoPanelRenderer?.actionPanel?.overlayPanelRenderer
private fun NavigationEndpointItem.getContent() = getOverlayPanel()?.content
private fun NavigationEndpointItem.getHeader() = getOverlayPanel()?.header

////////

fun MenuWrapper.getBrowseId() = menuRenderer?.items?.firstNotNullOfOrNull { it?.getBrowseId() }
fun MenuWrapper.getNotificationToken() = menuRenderer?.items?.firstNotNullOfOrNull { it?.getNotificationToken() }
fun MenuWrapper.getFeedbackTokens(): List<String?>? = menuRenderer?.items?.mapNotNull { it?.getFeedbackToken() }
// Filter by icon not robust. Icon item not always present.
fun MenuWrapper.getVideoToken() = menuRenderer?.items?.firstOrNull {
        it?.getIconType() == ICON_TYPE_NOT_INTERESTED
    }?.getFeedbackToken()
// Filter by icon not robust. Icon item not always present.
fun MenuWrapper.getChannelToken() = menuRenderer?.items?.firstOrNull {
        it?.getIconType() == ICON_TYPE_REMOVE
    }?.getFeedbackToken()

//////////

// gridVideoRenderer
fun VideoItem.getTitle() = title?.getText() ?: headline?.getText()
fun VideoItem.getVideoId() = videoId
fun VideoItem.getThumbnails() = thumbnail
fun VideoItem.getMovingThumbnails() = richThumbnail?.movingThumbnailRenderer?.movingThumbnailDetails
fun VideoItem.getDescBadgeText() = badges?.getOrNull(0)?.metadataBadgeRenderer?.label
fun VideoItem.getLengthText() = lengthText?.getText()
fun VideoItem.getPercentWatched() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayResumePlaybackRenderer?.percentDurationWatched }
fun VideoItem.getBadgeText() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.text?.getText() } ?:
    badges?.firstNotNullOfOrNull { it?.liveBadge?.label?.getText() ?: it?.upcomingEventBadge?.label?.getText() }
fun VideoItem.getUserName() = shortBylineText?.getText() ?: longBylineText?.getText()
fun VideoItem.getPublishedTimeText() = publishedTimeText?.getText()
fun VideoItem.getViewCount() = shortViewCountText?.getText() ?: viewCountText?.getText()
// No real date, just placeholder. We should do this themselves.
fun VideoItem.getUpcomingEventText() = upcomingEventData?.upcomingEventText?.getText()
    ?.replace("DATE_PLACEHOLDER", Helpers.toShortDate(upcomingEventData.getStartTimeMs()))
fun VideoItem.getChannelId() =
    shortBylineText?.runs?.firstNotNullOfOrNull { it?.navigationEndpoint?.getBrowseId() } ?:
    longBylineText?.runs?.firstNotNullOfOrNull { it?.navigationEndpoint?.getBrowseId() } ?:
    menu?.getBrowseId()
fun VideoItem.getPlaylistId() = navigationEndpoint?.watchEndpoint?.playlistId
fun VideoItem.getPlaylistIndex() = navigationEndpoint?.watchEndpoint?.index
fun VideoItem.isLive(): Boolean = OLD_BADGE_STYLE_LIVE == getOldBadgeStyle() || BADGE_STYLE_LIVE == getBadgeStyle()
fun VideoItem.isUpcoming() = BADGE_STYLE_UPCOMING == getBadgeStyle()
fun VideoItem.isShorts() = BADGE_STYLE_SHORTS == getBadgeStyle()
fun VideoItem.isMovie() = BADGE_STYLE_MOVIE == getBadgeStyle()
fun VideoItem.getFeedbackTokens() = menu?.getFeedbackTokens()
private fun VideoItem.getOldBadgeStyle() = badges?.firstNotNullOfOrNull { it?.metadataBadgeRenderer?.style }
private fun VideoItem.getBadgeStyle() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.style }

////////////

fun MusicItem.getTitle() = primaryText?.getText()
fun MusicItem.getUserName() = secondaryText?.getText()
fun MusicItem.getThumbnails() = thumbnail
fun MusicItem.getVideoId() = navigationEndpoint?.watchEndpoint?.videoId
fun MusicItem.getPlaylistId() = navigationEndpoint?.watchEndpoint?.playlistId
fun MusicItem.getBadgeText() = lengthText?.getText()
fun MusicItem.getLengthText() = lengthText?.getText()
fun MusicItem.getViewsAndPublished() = tertiaryText?.getText()
fun MusicItem.getChannelId() = menu?.getBrowseId()
fun MusicItem.getPlaylistIndex() = navigationEndpoint?.watchEndpoint?.index
fun MusicItem.getDescBadgeText() = null
fun MusicItem.getViewsCountText() = null
fun MusicItem.getUpcomingEventText() = null
fun MusicItem.isLive() = false
fun MusicItem.isUpcoming() = false

///////////

fun TileItem.getTitle() = metadata?.tileMetadataRenderer?.title?.getText()
fun TileItem.getVideoId() = onSelectCommand?.watchEndpoint?.videoId
fun TileItem.getPlaylistId() = onSelectCommand?.watchEndpoint?.playlistId ?: onSelectCommand?.watchPlaylistEndpoint?.playlistId
fun TileItem.getPlaylistIndex() = 0
fun TileItem.getDescBadgeText() = metadata?.tileMetadataRenderer?.lines?.map { it?.lineRenderer?.items?.getOrNull(0)?.lineItemRenderer?.badge?.metadataBadgeRenderer?.label }?.firstOrNull()
fun TileItem.getBadgeText() = header?.tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.text?.getText() }
fun TileItem.getPercentWatched() = header?.tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayResumePlaybackRenderer?.percentDurationWatched }
fun TileItem.getUserName() = null
fun TileItem.getPublishedTime() = null
fun TileItem.getViewCountText() =
    YouTubeHelper.createInfo(*metadata?.tileMetadataRenderer?.lines?.map {
        ServiceHelper.combineItems(" ", *it?.lineRenderer?.items?.map { it?.lineItemRenderer?.text }?.toTypedArray() ?: emptyArray())
    }?.toTypedArray() ?: emptyArray()) ?: null
fun TileItem.getUpcomingEventText() = null
fun TileItem.getThumbnails() = header?.tileHeaderRenderer?.thumbnail
fun TileItem.getMovingThumbnails() = header?.tileHeaderRenderer?.let { it.movingThumbnail ?: it.onFocusThumbnail }
fun TileItem.getMovingThumbnailUrl() = header?.tileHeaderRenderer?.movingThumbnail?.thumbnails?.getOrNull(0)?.url
fun TileItem.getChannelId() = getMenu()?.getBrowseId()
fun TileItem.getFeedbackTokens() = getMenu()?.getFeedbackTokens()
fun TileItem.isLive() = BADGE_STYLE_LIVE == getBadgeStyle()
fun TileItem.getContentType() = contentType
fun TileItem.getRichTextTileText() = header?.richTextTileHeaderRenderer?.textContent?.get(0)?.getText()
fun TileItem.getContinuationKey() = onSelectCommand?.getContinuation()?.getKey()
fun TileItem.isUpcoming() = BADGE_STYLE_UPCOMING == getBadgeStyle()
fun TileItem.isMovie() = BADGE_STYLE_MOVIE == getBadgeStyle()
fun TileItem.isShorts() = false // TODO: not implemented
private fun TileItem.Header.getBadgeStyle() = tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.style }
private fun TileItem.Metadata.getBadgeStyle() = tileMetadataRenderer?.lines?.firstNotNullOfOrNull { it?.lineRenderer?.items?.firstNotNullOfOrNull { it?.lineItemRenderer?.badge?.metadataBadgeRenderer?.style } }
private fun TileItem.getMenu() = menu ?: onLongPressCommand?.showMenuCommand?.menu
private fun TileItem.getBadgeStyle() = header?.getBadgeStyle() ?: metadata?.getBadgeStyle()

////////////

private fun ItemWrapper.getVideoItem() = gridVideoRenderer ?: videoRenderer ?: pivotVideoRenderer ?: compactVideoRenderer ?: reelItemRenderer
private fun ItemWrapper.getMusicItem() = tvMusicVideoRenderer
private fun ItemWrapper.getChannelItem() = gridChannelRenderer ?: pivotChannelRenderer ?: compactChannelRenderer
private fun ItemWrapper.getPlaylistItem() = gridPlaylistRenderer ?: pivotPlaylistRenderer ?: compactPlaylistRenderer
private fun ItemWrapper.getRadioItem() = gridRadioRenderer ?: pivotRadioRenderer ?: compactRadioRenderer
private fun ItemWrapper.getTileItem() = tileRenderer

fun ItemWrapper.getType(): Int {
    if (getChannelItem() != null)
        return MediaItem.TYPE_CHANNEL
    if (getPlaylistItem() != null)
        return MediaItem.TYPE_PLAYLIST
    if (getRadioItem() != null)
        return MediaItem.TYPE_PLAYLIST
    if (getVideoItem() != null)
        return MediaItem.TYPE_VIDEO
    if (getMusicItem() != null)
        return MediaItem.TYPE_MUSIC
    if (getTileItem() != null)
        return when (getTileItem()?.getContentType()) {
            TILE_CONTENT_TYPE_CHANNEL -> MediaItem.TYPE_CHANNEL
            TILE_CONTENT_TYPE_PLAYLIST -> MediaItem.TYPE_PLAYLIST
            TILE_CONTENT_TYPE_VIDEO -> MediaItem.TYPE_VIDEO
            else -> MediaItem.TYPE_UNDEFINED
        }

    return MediaItem.TYPE_UNDEFINED;
}

fun ItemWrapper.getVideoId() = getVideoItem()?.getVideoId() ?: getMusicItem()?.getVideoId() ?: getTileItem()?.getVideoId()
fun ItemWrapper.getTitle() = getVideoItem()?.getTitle() ?: getMusicItem()?.getTitle() ?: getTileItem()?.getTitle()
fun ItemWrapper.getThumbnails() = getVideoItem()?.getThumbnails() ?: getMusicItem()?.getThumbnails() ?: getTileItem()?.getThumbnails()
fun ItemWrapper.getMovingThumbnails() = getVideoItem()?.getMovingThumbnails() ?: getTileItem()?.getMovingThumbnails()
fun ItemWrapper.getDescBadgeText() = getVideoItem()?.getDescBadgeText() ?: getMusicItem()?.getDescBadgeText() ?: getTileItem()?.getDescBadgeText()
fun ItemWrapper.getLengthText() = getVideoItem()?.getLengthText() ?: getMusicItem()?.getLengthText() ?: getTileItem()?.getBadgeText()
fun ItemWrapper.getPercentWatched() = getVideoItem()?.getPercentWatched() ?: getTileItem()?.getPercentWatched()
fun ItemWrapper.getBadgeText() = getVideoItem()?.getBadgeText() ?: getMusicItem()?.getBadgeText() ?: getTileItem()?.getBadgeText()
fun ItemWrapper.getUserName() = getVideoItem()?.getUserName() ?: getMusicItem()?.getUserName() ?: getTileItem()?.getUserName()
fun ItemWrapper.getPublishedTime() = getVideoItem()?.getPublishedTimeText() ?: getMusicItem()?.getViewsAndPublished() ?: getTileItem()?.getPublishedTime()
fun ItemWrapper.getViewCountText() = getVideoItem()?.getViewCount() ?: getMusicItem()?.getViewsCountText() ?: getTileItem()?.getViewCountText()
fun ItemWrapper.getUpcomingEventText() = getVideoItem()?.getUpcomingEventText() ?: getMusicItem()?.getUpcomingEventText() ?: getTileItem()?.getUpcomingEventText()
fun ItemWrapper.getPlaylistId() = getVideoItem()?.getPlaylistId() ?: getMusicItem()?.getPlaylistId() ?: getTileItem()?.getPlaylistId()
fun ItemWrapper.getChannelId() = getVideoItem()?.getChannelId() ?: getMusicItem()?.getChannelId() ?: getTileItem()?.getChannelId()
fun ItemWrapper.getPlaylistIndex() = getVideoItem()?.getPlaylistIndex() ?: getMusicItem()?.getPlaylistIndex() ?: getTileItem()?.getPlaylistIndex()
fun ItemWrapper.isLive() = getVideoItem()?.isLive() ?: getMusicItem()?.isLive() ?: getTileItem()?.isLive()
fun ItemWrapper.isUpcoming() = getVideoItem()?.isUpcoming() ?: getMusicItem()?.isUpcoming() ?: getTileItem()?.isUpcoming()
fun ItemWrapper.isMovie() = getVideoItem()?.isMovie() ?: getTileItem()?.isMovie()
fun ItemWrapper.isShorts() = reelItemRenderer != null || getVideoItem()?.isShorts() ?: getTileItem()?.isShorts() ?: false
fun ItemWrapper.getDescriptionText() = getTileItem()?.getRichTextTileText()
fun ItemWrapper.getContinuationKey() = getTileItem()?.getContinuationKey()
fun ItemWrapper.getFeedbackToken() = getFeedbackTokens()?.getOrNull(0)
fun ItemWrapper.getFeedbackToken2() = getFeedbackTokens()?.getOrNull(1)
private fun ItemWrapper.getFeedbackTokens() = getVideoItem()?.getFeedbackTokens() ?: getTileItem()?.getFeedbackTokens()

/////

fun DefaultServiceEndpoint.getChannelIds() = getSubscribeEndpoint()?.channelIds
fun DefaultServiceEndpoint.getParams() = getSubscribeEndpoint()?.params
private fun DefaultServiceEndpoint.getSubscribeEndpoint() =
    authDeterminedCommand?.authenticatedCommand?.subscribeEndpoint

/////

fun ToggleButtonRenderer.getSubscribeParams() = defaultServiceEndpoint?.getParams()
fun ToggleButtonRenderer.getUnsubscribeParams() = toggledServiceEndpoint?.unsubscribeEndpoint?.params

//////

fun SubscribeButtonRenderer.getParams() = serviceEndpoints?.firstNotNullOfOrNull { it?.getParams() }

//////

fun VideoItem.UpcomingEvent.getStartTimeMs() = startTime?.toLong()?.let { it * 1_000 } ?: -1

//////

fun IconItem.getType() = iconType

//////

fun MenuItem.getIconType() = menuServiceItemRenderer?.icon?.getType()
fun MenuItem.getFeedbackToken() = menuServiceItemRenderer?.serviceEndpoint?.feedbackEndpoint?.feedbackToken
fun MenuItem.getNotificationToken() = menuServiceItemRenderer?.serviceEndpoint?.recordNotificationInteractionsEndpoint?.serializedInteractionsRequest
fun MenuItem.getBrowseId() = menuNavigationItemRenderer?.navigationEndpoint?.getBrowseId()

//////

fun NotificationPreferenceButton.getItems() = subscriptionNotificationToggleButtonRenderer?.states
fun NotificationPreferenceButton.getCurrentStateId() = subscriptionNotificationToggleButtonRenderer?.currentStateId ?: -1
fun NotificationStateItem.getTitle() = inlineMenuButton?.buttonRenderer?.text?.getText()
fun NotificationStateItem.getStateId() = stateId
fun NotificationStateItem.getStateParams() = inlineMenuButton?.buttonRenderer?.serviceEndpoint?.modifyChannelNotificationPreferenceEndpoint?.params