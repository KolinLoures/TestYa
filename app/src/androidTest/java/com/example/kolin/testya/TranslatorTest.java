package com.example.kolin.testya;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.kolin.testya.veiw.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by kolin on 21.04.2017.
 */
@RunWith(AndroidJUnit4.class)
public class TranslatorTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    private static final String textToTranslate = "привет";
    private static final String textResultTranslate = "hi";
    private static final int sizeSupLangiages = 90;

    @Test
    public void testTranslation() {
        //type text
        onView(withId(R.id.translator_edit_text)).perform(ViewActions.replaceText(textToTranslate));
        //wait a bit
        Util.pauseTestFor(700);
        //check translation is displayed
        onView(withId(R.id.translation_card))
                .check(ViewAssertions.matches(isDisplayed()));
        //check translation result
        onView(withId(R.id.translation_card_text_result))
                .check(ViewAssertions.matches(withText(textResultTranslate)));

        //wait a bit
        Util.pauseTestFor(1500);
        //check dictionary card is displaying
        onView(withId(R.id.dictionary_card))
                .check(ViewAssertions.matches(isDisplayed()));
        //check text in dictionary card
        onView(withId(R.id.dictionary_card_text_header))
                .check(ViewAssertions.matches(withText(textToTranslate)));
        //check that dictionary contains smth
        onView(withId(R.id.dictionary_card_recycler_view))
                .check(ViewAssertions.matches(hasDescendant(withText(textResultTranslate))));
    }

    @Test
    public void textDictionaryRecyclerViewClick() {
        //type text
        onView(withId(R.id.translator_edit_text)).perform(ViewActions.replaceText(textToTranslate));
        //wait a bit
        Util.pauseTestFor(3000);
        onView(withId(R.id.dictionary_card_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        //check edit text contains
        onView(withId(R.id.translator_edit_text)).check(ViewAssertions.matches(withText(textResultTranslate)));
    }

    @Test
    public void testSelectLanguageFromDialog(){
        //perfom click button that opens language dialog
        onView(withId(R.id.translation_btn_from)).perform(click());
        //check recycler view in dialog is display
        onView(withId(R.id.dialog_language_rv)).check(ViewAssertions.matches(isDisplayed()));
        //check size of adapter in recycler view
        onView(withId(R.id.dialog_language_rv)).check(ViewAssertions.matches(Util.checkListSize(sizeSupLangiages + 1)));
        //check "english" language in recycler view
        onView(withId(R.id.dialog_language_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //click yes button in dialog
        onView(withId(R.id.dialog_btn_yes)).perform(click());
        //assert that button contains text "english" language
        onView(withId(R.id.translation_btn_from)).check(ViewAssertions.matches(withText(R.string.determine_language)));
    }
}
