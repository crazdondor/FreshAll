package com.freshall.freshall;

import com.freshall.freshall.Controller.CreateNewPostActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TestNewPostInputTitle {

    private String textToInput;

    @Rule
    public ActivityTestRule<CreateNewPostActivity> newPostActivityActivityTestRule
            = new ActivityTestRule(CreateNewPostActivity.class);

    @Before
    public void initValidTest() {
        textToInput = "Cucumbers";
    }

    @Test
    public void testTitleText() {
        // Type text
        onView(withId(R.id.titleText)).perform(typeText(textToInput), closeSoftKeyboard());

        // check that the text was changed
        onView(withId(R.id.titleText)).check(matches(withText(textToInput)));
    }
}