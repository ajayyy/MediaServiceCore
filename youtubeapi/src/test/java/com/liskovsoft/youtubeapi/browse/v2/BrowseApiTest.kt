package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.browse.v1.BrowseApiHelper
import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.browse.v2.impl.MediaGroupImplKidsSection
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2
import com.liskovsoft.youtubeapi.common.models.gen.getFeedbackToken
import com.liskovsoft.youtubeapi.common.models.gen.getFeedbackToken2
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class BrowseApiTest {
    /**
     * Authorization should be updated each hour
     */
    private var mService: BrowseApi? = null

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mService = RetrofitHelper.withGson(BrowseApi::class.java)
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpersV2.getAuthorization()
        RetrofitOkHttpHelper.disableCompression = true
    }

    @Test
    fun testThatSubsNotEmpty() {
        val subs = getSubs()

        assertNotNull("Contains videos", subs?.getItems()?.getOrNull(0))
    }

    @Test
    fun testThatSubContainsFeedbackToken() {
        val subs = getSubs()

        assertNotNull("Contains feedback token", subs?.getItems()?.getOrNull(0)?.getFeedbackToken())
    }

    @Test
    fun testThatSubsCanBeContinued() {
        val subs = getSubs()

        assertNotNull("Contains continuation token", subs?.getContinuationToken())

        checkContinuation(subs?.getContinuationToken())
    }

    @Test
    fun testThatHomeNotEmpty() {
        val home = getHome()

        assertNotNull("Contains videos", home?.getItems()?.getOrNull(0))
    }

    @Test
    fun testThatHomeCanBeContinued() {
        val home = getHome()

        assertNotNull("Contains continuation token", home?.getContinuationToken())

        checkContinuation(home?.getContinuationToken())
    }

    @Test
    fun testThatHomeContainsAllTokens() {
        val home = getHome()

        val item = home?.getItems()?.firstOrNull { it?.getFeedbackToken() != null }

        assertNotNull("Home contains feedback token 1", item?.getFeedbackToken())
        assertNotNull("Home contains feedback token 2", item?.getFeedbackToken2())
    }

    @Test
    fun testThatHomeContainsSections() {
        val home = getHome()

        val sections = home?.getSections()

        val section = sections?.get(0)

        assertNotNull("Section contains title", section?.getTitle())
        assertNotNull("Section contains items", section?.getItems()?.get(0))
    }

    @Test
    fun testThatChipsCanBeContinued() {
        val home = getHome()

        val chips = home?.getChips()

        assertNotNull("Contains chips", chips)

        val chip = chips?.getOrNull(2) // first chip is empty, second one is Posts

        assertNotNull("Chip has title", chip?.getTitle())

        checkContinuation(chip?.getContinuationToken())
    }

    @Ignore("Doesn't contains chips")
    @Test
    fun testThatAltHomeNotEmpty() {
        val home = getAltHome()

        assertNotNull("Contains videos", home?.getItems()?.getOrNull(0))
    }

    @Test
    fun testThatKidsHomeNotEmpty() {
        val kidsHome = getKidsHome()

        assertNotNull("Contains sections", kidsHome?.getSections())
    }

    @Test
    fun testThatKidsHomeCanBeContinued() {
        val kidsHome = getKidsHome()

        kidsHome?.getSections()?.forEach {
            if (it?.getItems() == null) {
                val home = getKidsHome(it?.getBrowseParams())
                assertNotNull("Section not empty", home?.getRootSection()?.getItems())
            }
        }
    }

    @Test
    fun testKidsMediaItemConversion() {
        val kidsHome = getKidsHome()

        val mediaGroup = MediaGroupImplKidsSection(kidsHome?.getRootSection()!!)

        BrowseTestHelper.checkMediaItem(mediaGroup.mediaItems?.getOrNull(0)!!)
    }

    @Test
    fun testThatGuideNotEmpty() {
        val guide = getGuide()

        assertTrue("Guide contains channels", guide?.getFirstItems()?.isNotEmpty() == true)
        assertTrue("Guide collapse contains channels", guide?.getCollapsibleItems()?.size ?: 0 > 20)
    }

    @Test
    fun testThatReelsNotEmpty() {
        val reel = getReel()

        assertNotNull("Contains basic info", reel?.getWatchEndpoint())
        assertNotNull("Contains extended info", reel?.getPlayerHeader())
        assertNotNull("Contains continuation info", reel?.sequenceContinuation)
    }

    @Test
    fun testThatReelDetailsNotEmpty() {
        val reels = getReel()

        val continuation = getReelContinuation(reels?.sequenceContinuation)

        val firstEntry = continuation?.getItems()?.getOrNull(0)
        val details = getReelDetails(firstEntry?.videoId, firstEntry?.params)

        // Not present
        //assertNotNull("Contains basic info", details?.getWatchEndpoint())
        assertNotNull("Contains extended info", details?.getPlayerHeader())
        // Not present
        //assertNotNull("Contains continuation info", details?.continuationEndpoint)
    }

    private fun checkContinuation(token: String?) {
        val continuationResult = mService?.getContinuationResult(BrowseApiHelper.getContinuationQueryWeb(token))

        val continuation = RetrofitHelper.get(continuationResult)

        assertNotNull("Contains items", continuation?.getItems()?.getOrNull(0))

        assertNotNull("Contains continuation token", continuation?.getContinuationToken())
    }

    private fun getSubs(): BrowseResult? {
        val subsResult = mService?.getBrowseResult(BrowseApiHelper.getSubscriptionsQueryWeb())

        return RetrofitHelper.get(subsResult)
    }

    private fun getHome(): BrowseResult? {
        val homeResult = mService?.getBrowseResult(BrowseApiHelper.getHomeQueryWeb())

        return RetrofitHelper.get(homeResult)
    }

    private fun getAltHome(): BrowseResult? {
        val homeResult = mService?.getBrowseResultMobile(BrowseApiHelper.getHomeQueryMWEB())

        return RetrofitHelper.get(homeResult)
    }

    private fun getGuide(): GuideResult? {
        val guideResult = mService?.getGuideResult(ServiceHelper.createQueryWeb(""))

        return RetrofitHelper.get(guideResult)
    }

    private fun getKidsHome(): BrowseResultKids? {
        val kidsResult = mService?.getBrowseResultKids(BrowseApiHelper.getKidsHomeQuery())

        return RetrofitHelper.get(kidsResult)
    }

    private fun getKidsHome(params: String?): BrowseResultKids? {
        val kidsResult = mService?.getBrowseResultKids(BrowseApiHelper.getKidsHomeQuery(params))

        return RetrofitHelper.get(kidsResult)
    }

    private fun getReel(): ReelResult? {
        val reelsResult = mService?.getReelResult(BrowseApiHelper.getReelQuery())

        return RetrofitHelper.get(reelsResult)
    }

    private fun getReelDetails(videoId: String?, params: String?): ReelResult? {
        val details = mService?.getReelResult(BrowseApiHelper.getReelDetailsQuery(videoId, params))

        return RetrofitHelper.get(details)
    }

    private fun getReelContinuation(sequenceParams: String?): ReelContinuationResult? {
        val continuation = mService?.getReelContinuationResult(BrowseApiHelper.getReelContinuationQuery(sequenceParams))

        return RetrofitHelper.get(continuation)
    }

    private fun getReelContinuation2(nextPageKey: String?): ReelContinuationResult? {
        val continuation = mService?.getReelContinuationResult(BrowseApiHelper.getReelContinuation2Query(nextPageKey))

        return RetrofitHelper.get(continuation)
    }
}