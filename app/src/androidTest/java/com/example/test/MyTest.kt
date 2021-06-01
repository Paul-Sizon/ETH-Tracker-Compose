package com.example.test

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MyTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComposeMainActivity>()


    @Test
    fun button_is_present() {
        composeTestRule.onNodeWithText("GET LATEST PRICE").assertExists()
    }

    @Test
    fun progressBar_shows_and_goes_away_in_3_seconds() {
        composeTestRule.onNodeWithText("GET LATEST PRICE").performClick()
        composeTestRule.onNode(hasTestTag("progressBar")).assertIsDisplayed()
        CoroutineScope(Dispatchers.Default).launch {
            delay(3100)
            composeTestRule.onNode(hasTestTag("progressBar")).assertIsNotDisplayed()
        }
    }


}