package com.example.des.samplehomescreen;


import android.content.Context;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.des.studentmanagerredux.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginTestSuccessful2 {

    CountingIdlingResource idleres;

    @Rule
    public ActivityTestRule<Login> mActivityTestRule = new ActivityTestRule<>(Login.class);

    @Before
    public void setup()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();

        idleres = new CountingIdlingResource("Login");
        Espresso.registerIdlingResources(idleres);
    }

    @Test
    public void loginTestSuccessful2() {

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.etUsername), isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.etUsername), isDisplayed()));
        appCompatEditText2.perform(replaceText("des@email.ca"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.etPassword), isDisplayed()));
        appCompatEditText3.perform(replaceText("dddddd"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.bLogin), withText("Login"), isDisplayed()));
        appCompatButton.perform(click());

        idleres.increment();

        new Thread()
        {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                idleres.decrement();
            }
        }.start();

        /* THIS IS WHERE YOU MUST MANUALLY EDIT THE TEST*/
        // go to home screen
        ViewInteraction textView = onView(withId(R.id.activity_home_screen));

        // see if the homescreen has a child with text "Student Manager"
        textView.check(matches(withChild(withText("Student Manager"))));

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
