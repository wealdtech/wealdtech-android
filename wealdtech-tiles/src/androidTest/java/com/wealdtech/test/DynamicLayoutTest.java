package com.wealdtech.test;

import android.test.ActivityInstrumentationTestCase2;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

public class DynamicLayoutTest extends ActivityInstrumentationTestCase2<DynamicLayoutTestActivity>
{
  public DynamicLayoutTest()
  {
    super(DynamicLayoutTestActivity.class);
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    setActivityInitialTouchMode(false);
  }

  public void testNull()
  {
    final DynamicLayoutTestActivity activity = getActivity();
    activity.finish();
  }

  public void testEspresso()
  {
    final DynamicLayoutTestActivity activity = getActivity();
    onView(withText("Test 1")).check(matches(withText("Test 1")));
    activity.finish();
  }
}
