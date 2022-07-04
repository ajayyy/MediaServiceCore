package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import com.liskovsoft.mediaserviceinterfaces.ManagersFrontend;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV1;
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2;
import com.liskovsoft.youtubeapi.service.YouTubeManagersFrontend;
import com.liskovsoft.youtubeapi.service.YouTubeSignInManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class YouTubeMPDBuilderInstrumentedTest {
    private ManagersFrontend mService;

    @Before
    public void setUp() {
        mService = YouTubeManagersFrontend.instance();
    }

    @After
    public void tearDown() {
        YouTubeSignInManager.instance().setAuthorizationHeader(null);
    }

    @Test
    public void testThatCipheredFormatIsValid() {
        testVideoFormatUrl(TestHelpersV1.VIDEO_ID_MUSIC_2);
    }

    @Test
    public void testThatSignedCipheredFormatIsValid() {
        YouTubeSignInManager.instance().setAuthorizationHeader(TestHelpersV2.getAuthorization());

        testVideoFormatUrl(TestHelpersV1.VIDEO_ID_MUSIC_2);
    }

    @Test
    public void testThatSimpleFormatIsValid() throws IOException {
        testVideoFormatUrl(TestHelpersV1.VIDEO_ID_CAPTIONS);
    }

    @Test
    public void testThatMpdNotEmpty() {
        MediaItemFormatInfo mediaItemDetails = mService.getMediaItemManager().getFormatInfo(TestHelpersV1.VIDEO_ID_CAPTIONS);

        assertTrue("Is dash", mediaItemDetails.containsDashInfo());

        InputStream mpdStream = mediaItemDetails.createMpdStream();

        assertNotNull("Mpd stream not null", mpdStream);
        String mpdContent = Helpers.toString(mpdStream);
        assertFalse("Mpd content not empty", mpdContent.isEmpty());
    }

    private MediaItemFormatInfo getMediaItemDetails() {
        List<MediaGroup> homeGroups = mService.getMediaGroupManager().getHome();

        List<MediaItem> mediaItems = homeGroups.get(0).getMediaItems();

        assertTrue("Media item not empty", mediaItems != null && mediaItems.size() > 0);

        MediaItem mediaItem = mediaItems.get(0);

        return mService.getMediaItemManager().getFormatInfo(mediaItem);
    }

    private void testVideoFormatUrl(String videoId) {
        MediaItemFormatInfo mediaItemDetails = mService.getMediaItemManager().getFormatInfo(videoId);

        assertNotNull("Format info not empty", mediaItemDetails);
        assertTrue("Format list not empty", mediaItemDetails.getAdaptiveFormats().size() > 0);

        String url = mediaItemDetails.getAdaptiveFormats().get(0).getUrl();

        assertTrue("Video url is working", TestHelpersV1.urlExists(url));
    }
}