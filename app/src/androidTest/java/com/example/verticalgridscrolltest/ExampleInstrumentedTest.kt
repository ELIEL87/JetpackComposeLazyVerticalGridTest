package com.example.verticalgridscrolltest

import android.view.KeyEvent.*
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performKeyPress
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.verticalgridscrolltest.ui.theme.VerticalGridScrollTestTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalAnimationApi
    @Test
    fun testLazyVerticalGrid() {
        composeTestRule.run {
            setContent {
                VerticalGridScrollTestTheme {
                    val gridItems = (1..50).map { GridItem(it.toString()) }
                    LazyVerticalGrid(
                        items = gridItems,
                        gridItemContent = gridItemContent
                    )
                }
            }
            fun performKeyvent(key: Int) {
                onNodeWithTag("browsable-media-container", useUnmergedTree = true).performKeyPress(
                    keyEvent = KeyEvent(android.view.KeyEvent(ACTION_DOWN, key))
                )
                onNodeWithTag("browsable-media-container", useUnmergedTree = true).performKeyPress(
                    keyEvent = KeyEvent(android.view.KeyEvent(ACTION_UP, key))
                )
            }
            onNodeWithTag("browsable-media-container", useUnmergedTree = true).assertIsDisplayed()

            performKeyvent(KEYCODE_DPAD_RIGHT)
            performKeyvent(KEYCODE_DPAD_RIGHT)
            (1..4).onEach {
                performKeyvent(KEYCODE_DPAD_DOWN)
            }
            (1..5).onEach {
                performKeyvent(KEYCODE_DPAD_UP)
            }
            (1..3).onEach {
                performKeyvent(KEYCODE_DPAD_DOWN)
            }
            (1..4).onEach {
                performKeyvent(KEYCODE_DPAD_UP)
            }
            (1..3).onEach {
                performKeyvent(KEYCODE_DPAD_DOWN)
            }
            (1..5).onEach {
                performKeyvent(KEYCODE_DPAD_UP)
            }
            (1..3).onEach {
                performKeyvent(KEYCODE_DPAD_DOWN)
            }
            (1..5).onEach {
                performKeyvent(KEYCODE_DPAD_UP)
            }
        }
    }
}
