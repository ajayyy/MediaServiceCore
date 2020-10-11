package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.BaseData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class AppManagerTest {
    private AppManagerWrapper mManager;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mManager = new AppManagerWrapper();
    }

    @Test
    public void testThatAppInfoContainsAllRequiredFields() throws IOException {
        String playerUrl = getPlayerUrl();
        assertTrue("Player url should ends with js", playerUrl.endsWith(".js"));
    }

    @Test
    public void testThatDecipherFunctionIsValid() {
        String playerUrl = getPlayerUrl();

        PlayerData playerData = mManager.getPlayerData(playerUrl);

        assertNotNull("Decipher result not null", playerData);

        String decipherFunctionContent = playerData.getDecipherFunction();
        assertNotNull("Decipher function not null", decipherFunctionContent);
        assertFalse("Decipher function is not empty", decipherFunctionContent.isEmpty());
        assertTrue("Decipher function has proper content",
                decipherFunctionContent.startsWith("var ") && decipherFunctionContent.contains("function ") &&
                        decipherFunctionContent.endsWith(".join(\"\")}"));
    }

    @Test
    public void testThatPlaybackNonceFunctionIsValid() {
        String playerUrl = getPlayerUrl();

        PlayerData clientPlaybackNonceFunction = mManager.getPlayerData(playerUrl);

        assertNotNull("Playback nonce result not null", clientPlaybackNonceFunction);

        String playbackNonceFunctionContent = clientPlaybackNonceFunction.getClientPlaybackNonce();
        assertNotNull("Playback nonce function not null", playbackNonceFunctionContent);
        assertFalse("Playback nonce function not empty", playbackNonceFunctionContent.isEmpty());
        assertTrue("Playback nonce has valid content", playbackNonceFunctionContent.startsWith(";function ") &&
                playbackNonceFunctionContent.contains("\nfunction ") && playbackNonceFunctionContent.endsWith(".join(\"\")}"));
    }

    @Test
    public void testThatClientIdAndSecretNotEmpty() {
        String baseUrl = getBaseUrl();

        BaseData baseData = mManager.getBaseData(baseUrl);

        assertNotNull("Base data not null", baseData);

        assertNotNull("Client id not empty", baseData.getClientId());
        assertNotNull("Client secret not empty", baseData.getClientSecret());
    }

    private String getPlayerUrl() {
        AppInfo appInfo = mManager.getAppInfo(AppConstants.USER_AGENT_COBALT);

        assertNotNull("AppInfo not null", appInfo);

        String playerUrl = appInfo.getPlayerUrl();

        assertNotNull("Player url not null", playerUrl);

        return AppConstants.SCRIPTS_URL_BASE + playerUrl.replace("\\/", "/");
    }

    private String getBaseUrl() {
        AppInfo appInfo = mManager.getAppInfo(AppConstants.USER_AGENT_COBALT);

        assertNotNull("AppInfo not null", appInfo);

        String baseUrl = appInfo.getBaseUrl();

        assertNotNull("Base url not null", baseUrl);

        return AppConstants.SCRIPTS_URL_BASE + baseUrl.replace("\\/", "/");
    }
}