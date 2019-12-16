package com.example.buddii.ui.login;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.buddii.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class babtapuserbase {

    @Rule
    public ActivityTestRule<loginActivity> mActivityTestRule = new ActivityTestRule<>(loginActivity.class);

    @Test
    public void babtapuserbase() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.Testing),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.beABuddii),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.parent),
                        childAtPosition(
                                allOf(withId(R.id.BeBudList),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.toolbar_layout),
                        isDisplayed()));
        frameLayout.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.UserHome),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.UserInfo),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.refreshButton),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction frameLayout2 = onView(
                allOf(withId(R.id.mapBuddii),
                        isDisplayed()));
        frameLayout2.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.userLocation),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.userLocation),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));


    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
