package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.common.models.gen.NavigationEndpointItem
import com.liskovsoft.youtubeapi.common.models.gen.TextItem
import com.liskovsoft.youtubeapi.common.models.gen.ThumbnailItem

data class Section(
    val itemSectionRenderer: ItemSectionRenderer?,
    val richItemRenderer: RichItemRenderer?,
    val richSectionRenderer: RichSectionRenderer?,
    val continuationItemRenderer: ContinuationItemRenderer?
)

// WhatToWatch only
data class RichSectionRenderer(
    val content: Content?
) {
    data class Content(
        val richShelfRenderer: RichShelfRenderer?
    ) {
        data class RichShelfRenderer(
            val title: TextItem?,
            val contents: List<Content?>?
        ) {
            data class Content(
                val richItemRenderer: RichItemRenderer?,
                val continuationItemRenderer: ContinuationItemRenderer?
            )
        }
    }
}

// Subscriptions only
data class ItemSectionRenderer(
    val contents: List<Shelf?>?
) {
    data class Shelf(
        val shelfRenderer: ShelfRenderer?
    ) {
        data class ShelfRenderer(
            val content: Content?
        ) {
            data class Content(
                val gridRenderer: GridRenderer?,
                val expandedShelfContentsRenderer: ExpandedShelfContentsRenderer?
            ) {
                data class GridRenderer(
                    val items: List<ItemWrapper?>?
                )

                data class ExpandedShelfContentsRenderer(
                    val items: List<ItemWrapper?>?
                )
            }
        }
    }
}

// Common item (WhatToWatch, Subscriptions)
data class RichItemRenderer(
    val content: ItemWrapper?
)

// Common item (WhatToWatch, Subscriptions)
data class ContinuationItemRenderer(
    val continuationEndpoint: NavigationEndpoint?
)

data class ContinuationCommand(
    val token: String?
)

data class ChipCloudChipRenderer(
    val text: TextItem?,
    val navigationEndpoint: NavigationEndpoint? // possible duplicate?
)

data class NavigationEndpoint(
    val continuationCommand: ContinuationCommand?
)

/////

data class GuideItem(
    val thumbnail: ThumbnailItem?,
    val formattedTitle: TextItem?,
    val navigationEndpoint: NavigationEndpointItem?,
    val badges: Badges?,
    val presentationStyle: String?
) {
    data class Badges(
        val liveBroadcasting: Boolean?
    )
}

// Kids only
data class AnchoredSectionRenderer(
    val title: String?,
    val navigationEndpoint: NavigationEndpointItem?,
    val content: ContentItem?
) {
    data class ContentItem(
        val sectionListRenderer: SectionListRenderer?
    ) {
        data class SectionListRenderer(
            val contents: List<ContentItem?>?
        ) {
            data class ContentItem(
                val itemSectionRenderer: ItemSectionRenderer?
            ) {
                data class ItemSectionRenderer(
                    val contents: List<ItemWrapper?>?
                )
            }
        }
    }
}

// Reel only. Basic data. No title or description.
data class ReelWatchEndpoint(
    val videoId: String?,
    val playerParams: String?,
    val params: String?,
    val thumbnail: ThumbnailItem?
)

// Reel only. Extended data.
data class ReelPlayerHeaderRenderer(
    val reelTitleText: TextItem?,
    val timestampText: TextItem?,
    val channelNavigationEndpoint: NavigationEndpointItem?,
    val channelThumbnail: ThumbnailItem?
)