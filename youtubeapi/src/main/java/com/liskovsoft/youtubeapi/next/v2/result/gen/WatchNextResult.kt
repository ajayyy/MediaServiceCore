package com.liskovsoft.youtubeapi.next.v2.result.gen

data class WatchNextResult(
    val contents: Contents?,
    val transportControls: TransportControls?
) {
    data class Contents(
        val singleColumnWatchNextResults: SingleColumnWatchNextResults?
    ) {
        data class SingleColumnWatchNextResults(
            val pivot: Pivot?,
            val results: Results?,
            val autoplay: Autoplay?
        ) {
            data class Pivot(
                val pivot: Pivot?
            ) {
                data class Pivot(
                    val contents: List<Content?>?
                ) {
                    data class Content(
                        val shelfRenderer: ShelfRenderer?
                    ) {
                        data class ShelfRenderer(
                                val title: TextItem?,
                                val content: Content?
                        ) {


                            data class Content(
                                val horizontalListRenderer: HorizontalListRenderer?
                            ) {
                                data class HorizontalListRenderer(
                                    val items: List<ItemWrapper?>?
                                )
                            }
                        }
                    }
                }
            }

            data class Results(
                val results: Results?
            ) {
                data class Results(
                    val contents: List<Content?>?
                ) {
                    data class Content(
                        val itemSectionRenderer: ItemSectionRenderer?
                    ) {
                        data class ItemSectionRenderer(
                            val contents: List<Content?>?
                        ) {
                            data class Content(
                                    val videoMetadataRenderer: VideoMetadataItem?,
                                    val musicWatchMetadataRenderer: VideoMetadataItem?
                            )
                        }
                    }
                }
            }

            data class Autoplay(
                val autoplay: Autoplay?
            ) {
                data class Autoplay(
                    val sets: List<Set?>?,
                    val replayVideoRenderer: ItemWrapper?
                ) {
                    data class Set(
                        val nextVideoRenderer: NextVideoRenderer?
                    ) {
                        data class NextVideoRenderer(
                                val maybeHistoryEndpointRenderer: NextVideoItem?,
                                val autoplayEndpointRenderer: NextVideoItem?
                        )
                    }
                }
            }
        }
    }

    data class TransportControls(
        val transportControlsRenderer: ButtonState?
    )
}