package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.common.models.gen.NavigationEndpointItem
import com.liskovsoft.youtubeapi.common.models.gen.TextItem

/**
 * Based on:
 *
 * browse_subs_chrome_12.02.2023.json
 */
data class BrowseResult(
    val contents: Contents?
) {
    data class Contents(
        val twoColumnBrowseResultsRenderer: TwoColumnBrowseResultsRenderer?
    ) {
        data class TwoColumnBrowseResultsRenderer(
            val tabs: List<Tab?>?
        ) {
            data class Tab(
             val tabRenderer: TabRenderer?
            ) {
                data class TabRenderer(
                    val content: Content?
                ) {
                   data class Content(
                       val sectionListRenderer: SectionListRenderer?,
                       val richGridRenderer: RichGridRenderer?
                   ) {
                       data class SectionListRenderer(
                           val contents: List<Section?>?
                       )
                       data class RichGridRenderer(
                           val contents: List<Section?>?,
                           val header: Header?
                       ) {
                           data class Header(
                               val feedFilterChipBarRenderer: FeedFilterChipBarRenderer?
                           ) {
                               data class FeedFilterChipBarRenderer(
                                   val contents: List<Content?>?
                               ) {
                                   data class Content(
                                       val chipCloudChipRenderer: ChipCloudChipRenderer?
                                   )
                               }
                           }
                       }
                   }
                }
            }
        }
    }
}

data class ContinuationResult(
    val onResponseReceivedActions: List<OnResponseReceivedAction?>?
) {
    data class OnResponseReceivedAction(
        val appendContinuationItemsAction: AppendContinuationItemsAction?,
        val reloadContinuationItemsCommand: ReloadContinuationItemsCommand?
    ) {
        data class AppendContinuationItemsAction(
            val continuationItems: List<Section?>?
        )

        data class ReloadContinuationItemsCommand(
            val continuationItems: List<Section?>?
        )
    }
}

data class GuideResult(
    val items: List<Item?>?
) {
    data class Item(
        val guideSubscriptionsSectionRenderer: GuideSubscriptionsSectionRenderer?
    ) {
        data class GuideSubscriptionsSectionRenderer(
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

data class BrowseResultKids(
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
