package com.wealdtech.android.fabric.matcher;

import android.view.View;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.wealdtech.Preconditions.checkNotNull;

/**
 */
public class ViewMatchers
{
  /**
   * Returns a matcher that matches {@link View}s based on resource ids. Note: Android resource ids are not guaranteed to be unique.
   * You may have to pair this matcher with another one to guarantee a unique view selection.
   *
   * @param integerMatcher a Matcher for resource ids
   */
  public static Matcher<View> withId(final Matcher<Integer> integerMatcher)
  {
    checkNotNull(integerMatcher);
    return new TypeSafeMatcher<View>()
    {
      @Override
      public void describeTo(Description description)
      {
        description.appendText("with id: ");
        integerMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view)
      {
        return integerMatcher.matches(view.getId());
      }
    };
  }

}
