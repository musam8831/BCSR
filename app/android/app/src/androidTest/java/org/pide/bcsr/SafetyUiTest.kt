package org.pide.bcsr

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Safety UI on an emulator (T-M5-026; TST-01 instrumented level). Covers TC-SR-01b/01c and AG-10 R-1, R-4, R-13.
 * Real-device and OEM behaviour (dialer, battery, process death by the system) stay with T-M5-014.
 */
@RunWith(AndroidJUnit4::class)
class SafetyUiTest {
    @get:Rule val compose = createEmptyComposeRule()
    private val app = ApplicationProvider.getApplicationContext<BcsrApp>()
    private fun s(id: Int, vararg args: Any) = app.getString(id, *args)
    private val emergency get() = s(R.string.call_emergency, "1122")

    @Before fun freshOnboardedStore() {
        app.startFresh()
        app.container!!.settings().onboarded = true
    }

    @After fun clean() = app.startFresh()

    private fun launch() = ActivityScenario.launch(MainActivity::class.java)

    /** SAF-01 v1.2 E2 (R-2): one tap to help from every screen; not shown on the safety screen itself. */
    @Test fun helpIsOneTapFromEveryScreen() {
        launch().use { sc ->
            for (screen in listOf(R.string.library, R.string.privacy, R.string.about)) {
                compose.onNodeWithText(s(screen)).performClick()
                compose.onNodeWithText(s(R.string.need_help)).assertExists()
                sc.onActivity { it.onBackPressedDispatcher.onBackPressed() }
            }
            compose.onNodeWithText(s(R.string.need_help)).performClick()
            compose.onNodeWithText(s(R.string.safety_heading)).assertIsDisplayed()
            compose.onNodeWithText(emergency).assertExists()
            compose.onNodeWithText(s(R.string.need_help)).assertDoesNotExist()
        }
    }

    /** SAF-01 §3, AG-10 R-13 (rendered order): help above "I'm safe for now"; no du'a without an approved item. */
    @Test fun helpComesBeforeSafeForNow() {
        launch().use {
            compose.onNodeWithText(s(R.string.need_help)).performClick()
            val call = compose.onNodeWithText(emergency).getUnclippedBoundsInRoot()
            val safe = compose.onNodeWithText(s(R.string.safe_for_now)).getUnclippedBoundsInRoot()
            assertTrue("emergency ${call.top} must be above safe ${safe.top}", call.top < safe.top)
            compose.onNodeWithText(s(R.string.safe_for_now)).performClick()
            compose.onNodeWithText(s(R.string.offer_dua)).assertDoesNotExist() // no approved CS-10 item ships yet
            compose.onNodeWithText(s(R.string.back)).assertExists()
        }
    }

    /** AG-10 R-1: the safety screen survives activity recreation, and the visit is counted once. */
    @Test fun safetyScreenSurvivesRecreation() {
        launch().use { sc ->
            compose.onNodeWithText(s(R.string.need_help)).performClick()
            compose.waitUntil(5_000) { app.container!!.settings().safetyCount == 1 }
            sc.recreate()
            compose.onNodeWithText(s(R.string.safety_heading)).assertIsDisplayed()
            compose.onNodeWithText(emergency).assertExists()
            compose.waitForIdle()
            assertEquals(1, app.container!!.settings().safetyCount)
        }
    }

    /** SAF-01 v1.2 §3, AG-10 R-4: a check-in during the 24 h quiet shows the helplines, not a du'a or "no du'a". */
    @Test fun checkInDuringQuietShowsHelp() {
        app.container!!.settings().quietAfterSafetyUntil = System.currentTimeMillis() + 3_600_000L
        launch().use {
            compose.onNodeWithText(s(R.string.how_am_i)).performClick()
            compose.onNodeWithText(s(R.string.save)).performScrollTo().performClick() // below the fold on the check-in
            compose.onNodeWithText(s(R.string.safety_body)).assertIsDisplayed()
            compose.onNodeWithText(emergency).assertExists()
            compose.onNodeWithText(s(R.string.no_item)).assertDoesNotExist()
        }
    }

    /** T-M5-025 / TC-SR-01c: an unreadable store shows Locked with the helplines; start fresh leads to onboarding. */
    @Test fun corruptKeyShowsLockedWithHelplines() {
        app.container!!.store.close()
        File(app.filesDir, "bcsr-db.key").writeBytes(ByteArray(5))
        app.openContainer()
        assertNull(app.container)
        assertTrue(app.lockedPermanently)
        launch().use {
            compose.onNodeWithText(s(R.string.locked_title)).assertIsDisplayed()
            compose.onNodeWithText(emergency).assertExists()
            compose.onNodeWithText(s(R.string.start_fresh)).performClick()
            compose.onAllNodesWithText(s(R.string.start_fresh)).onLast().performClick() // the confirm dialog's button
            compose.onNodeWithText(s(R.string.welcome_title)).assertIsDisplayed()
        }
    }
}
