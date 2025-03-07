package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.common.models.gen.MenuWrapper
import com.liskovsoft.youtubeapi.common.models.gen.PlaylistItem
import com.liskovsoft.youtubeapi.next.v2.gen.ContinuationItem
import com.liskovsoft.youtubeapi.next.v2.gen.EngagementPanel
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResultContinuation

/**
 * Based on:
 *
 * browse_subs_chrome_12.02.2023.json
 */
internal data class BrowseResult(
    val contents: Contents?,
    val header: Header?
) {
    data class Contents(
        val twoColumnBrowseResultsRenderer: TwoColumnBrowseResultsRenderer?
    ) {
        data class TwoColumnBrowseResultsRenderer(
            val tabs: List<Tab?>?
        ) {
            data class Tab(
                val tabRenderer: TabRenderer?,
                val expandableTabRenderer: TabRenderer?
            )
        }
    }
    data class Header(
        val playlistHeaderRenderer: PlaylistItem?
    )
}

internal data class ContinuationResult(
    val onResponseReceivedActions: List<OnResponseReceivedAction?>?
) {
    data class OnResponseReceivedAction(
        val appendContinuationItemsAction: AppendContinuationItemsAction?,
        val reloadContinuationItemsCommand: ReloadContinuationItemsCommand?
    ) {
        data class AppendContinuationItemsAction(
            val continuationItems: List<SectionWrapper?>?
        )

        data class ReloadContinuationItemsCommand(
            val continuationItems: List<SectionWrapper?>?,
            val slot: String?
        )
    }
}

internal data class GuideResult(
    val items: List<Item?>?
) {
    data class Item(
        val guideSubscriptionsSectionRenderer: GuideSectionRenderer?,
        val guideSectionRenderer: GuideSectionRenderer?
    ) {
        data class GuideSectionRenderer(
            val items: List<GuideItemWrapper?>?
        ) {
            data class GuideItemWrapper(
                val guideEntryRenderer: GuideItem?,
                val guideCollapsibleEntryRenderer: GuideCollapsibleEntryRenderer?
            ) {
                data class GuideCollapsibleEntryRenderer(
                    val expandableItems: List<ExpandableItem?>?
                ) {
                    data class ExpandableItem(
                        val guideEntryRenderer: GuideItem?
                    )
                }
            }
        }
    }
}

internal data class BrowseResultKids(
    val contents: Contents?
) {
    data class Contents(
        val kidsHomeScreenRenderer: KidsHomeScreenRenderer?
    ) {
        data class KidsHomeScreenRenderer(
            val anchors: List<Anchor?>?
        ) {
            data class Anchor(
                val anchoredSectionRenderer: AnchoredSectionRenderer?
            )
        }
    }
}

internal data class ReelResult(
    val replacementEndpoint: ReplacementEndpoint?,
    val overlay: Overlay?,
    val sequenceContinuation: String?, // first continuation
    val continuationEndpoint: NavigationEndpoint?, // subsequent continuations
    val engagementPanels: List<EngagementPanel?>?
) {
    data class ReplacementEndpoint(
        val reelWatchEndpoint: ReelWatchEndpoint?
    )
    data class Overlay(
        val reelPlayerOverlayRenderer: ReelPlayerOverlayRenderer?
    ) {
        data class ReelPlayerOverlayRenderer(
            val reelPlayerHeaderSupportedRenderers: ReelPlayerHeaderSupportedRenderers?,
            val menu: MenuWrapper?
        ) {
            data class ReelPlayerHeaderSupportedRenderers(
                val reelPlayerHeaderRenderer: ReelPlayerHeaderRenderer?
            )
        }
    }
}

internal data class ReelContinuationResult(
    val entries: List<EntryItem?>?,
    val continuationEndpoint: NavigationEndpoint?,
    val continuation: String?
) {
    data class EntryItem(
        val command: Command?
    ) {
        data class Command(
            val reelWatchEndpoint: ReelWatchEndpoint?
        )
    }
}

internal data class BrowseResultTV(
    val contents: Contents?
) {
    data class Contents(
        val tvBrowseRenderer: TvBrowseRenderer?
    ) {
        data class TvBrowseRenderer(
            val content: Content?
        ) {
            data class Content(
                val tvSurfaceContentRenderer: TvSurfaceContentRenderer?
            ) {
                data class TvSurfaceContentRenderer(
                    val content: Content?
                ) {
                    data class Content(
                        val sectionListRenderer: ItemSectionRenderer?
                    )
                }
            }
        }
    }
}
