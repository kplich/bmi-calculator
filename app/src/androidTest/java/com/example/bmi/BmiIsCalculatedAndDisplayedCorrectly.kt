package com.example.bmi

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BmiIsCalculatedAndDisplayedCorrectly {
    companion object {
        const val INPUT_MASS = "83"
        const val INPUT_HEIGHT = "193"
        const val EXPECTED_BMI = "22.28"
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun bmiIsCalculatedAndDisplayed() {
        val massInputField = onView(withId(R.id.massInput))
        massInputField.perform(replaceText(INPUT_MASS), closeSoftKeyboard())

        val heightInputField = onView(withId(R.id.heightInput))
        heightInputField.perform(replaceText(INPUT_HEIGHT), closeSoftKeyboard())

        val countBmiButton = onView(withId(R.id.countBmiButton))
        countBmiButton.perform(click())

        val bmiResultText = onView(withId(R.id.bmiResult))
        bmiResultText.check(matches(withText(EXPECTED_BMI)))
        bmiResultText.check(matches(isDisplayed()))

        val bmiCategoryText = onView(withId(R.id.bmiCategory))
        bmiCategoryText.check(matches(withText(
            InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.normal)
        )))
        bmiCategoryText.check(matches(isDisplayed()))
    }
}