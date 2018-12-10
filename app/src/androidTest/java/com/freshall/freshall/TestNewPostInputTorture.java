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
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TestNewPostInputTorture {

    private String textToInput;

    @Rule
    public ActivityTestRule<CreateNewPostActivity> newPostActivityActivityTestRule
            = new ActivityTestRule(CreateNewPostActivity.class);

    @Before
    public void initValidTest() {
        textToInput = "àáâäæãåāèéêëēėęîïíīįìôöòóœøōõûüùúū" +
                "ÀÁÂÄÆÃÅĀÈÉÊËĒĖĘÎÏÍĪĮÌÔÖÒÓŒØŌÕÛÜÙÚŪ";
    }

    @Test
    public void tortureTextInputs() {
        // Type title text
        onView(withId(R.id.titleText)).perform(replaceText(textToInput), closeSoftKeyboard());

        // type location text
        onView(withId(R.id.locationText)).perform(replaceText(textToInput), closeSoftKeyboard());

        // type price text
        onView(withId(R.id.price)).perform(replaceText(textToInput), closeSoftKeyboard());

        // check that the title text was changed
        onView(withId(R.id.titleText)).check(matches(withText(textToInput)));

        // check that the location text was changed
        onView(withId(R.id.locationText)).check(matches(withText(textToInput)));

        // check that the price text was changed
        onView(withId(R.id.price)).check(matches(withText(textToInput)));
    }
}