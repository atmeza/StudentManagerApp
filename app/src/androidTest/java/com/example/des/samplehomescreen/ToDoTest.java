package com.example.des.samplehomescreen;


import android.app.usage.UsageEvents;
import android.content.Context;
import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.des.studentmanagerredux.R;
import com.example.des.studentmanagerredux.db.EventDbHelper;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ToDoTest {
    CountingIdlingResource idleres;
    EventDbHelper db;

    @Before
    public void setup()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();

        db = new EventDbHelper(appContext);
        idleres = new CountingIdlingResource("Login");
        Espresso.registerIdlingResources(idleres);
    }

    @Rule
    public ActivityTestRule<Login> mActivityTestRule = new ActivityTestRule<>(Login.class);

    @Test
    public void toDoTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.etUsername), isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.etUsername), isDisplayed()));
        appCompatEditText2.perform(replaceText("mhatch@ucsd.edu"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.etPassword), isDisplayed()));
        appCompatEditText3.perform(replaceText("Tr0ub4dor&3"), closeSoftKeyboard());

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

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button19), withText("To-Do List"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction editText = onView(
                allOf(withClassName(is("android.widget.EditText")),
                        withParent(allOf(withId(R.id.custom),
                                withParent(withId(R.id.customPanel)))),
                        isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withClassName(is("android.widget.EditText")),
                        withParent(allOf(withId(R.id.custom),
                                withParent(withId(R.id.customPanel)))),
                        isDisplayed()));
        editText2.perform(replaceText("Test Task 1"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton3.perform(click());

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

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withClassName(is("android.widget.EditText")),
                        withParent(allOf(withId(R.id.custom),
                                withParent(withId(R.id.customPanel)))),
                        isDisplayed()));
        editText3.perform(replaceText("Test Task 2"), closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        new Thread()
        {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                idleres.decrement();
            }
        }.start();

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction editText4 = onView(
                allOf(withClassName(is("android.widget.EditText")),
                        withParent(allOf(withId(R.id.custom),
                                withParent(withId(R.id.customPanel)))),
                        isDisplayed()));
        editText4.perform(replaceText("Test Task 1"), closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton5.perform(click());

        new Thread()
        {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                idleres.decrement();
            }
        }.start();

        ViewInteraction textView = onView(
                allOf(withId(R.id.alertTitle), withText("Error: New Task Name Already Exists"),
                        childAtPosition(
                                allOf(withId(R.id.title_template),
                                        childAtPosition(
                                                withId(R.id.topPanel),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Error: New Task Name Already Exists")));

        new Thread()
        {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                idleres.decrement();
            }
        }.start();

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button1), withText("Cancel"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.task_item_checkBox), isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction floatingActionButton4 = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton4.perform(click());

        ViewInteraction editText5 = onView(
                allOf(withClassName(is("android.widget.EditText")),
                        withParent(allOf(withId(R.id.custom),
                                withParent(withId(R.id.customPanel)))),
                        isDisplayed()));
        editText5.perform(replaceText("Test Task 3"), closeSoftKeyboard());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatCheckBox2 = onView(
                allOf(withId(R.id.task_item_checkBox), isDisplayed()));
        appCompatCheckBox2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.task_item_view_title), withText("Test Task 1"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Test Task 1")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.task_item_view_title), withText("Test Task 2"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Test Task 2")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.task_item_view_title), withText("Test Task 3"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Test Task 3")));

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.task_item_view_title), withText("Test Task 1"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(android.R.id.button2), withText("Cancel"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.task_item_view_delete), withText("Delete"), isDisplayed()));
        appCompatButton9.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.task_item_view_title), withText("Test Task 1"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                0),
                        isDisplayed()));
        textView5.check(doesNotExist());

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
