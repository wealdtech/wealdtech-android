package com.wealdtech.android.test;

import android.test.ActivityInstrumentationTestCase2;

public class DynamicLayoutTest extends ActivityInstrumentationTestCase2<DynamicLayoutTestActivity>
{
  public DynamicLayoutTest()
  {
    super(DynamicLayoutTestActivity.class);
  }

//  @Override
//  protected void setUp() throws Exception
//  {
//    super.setUp();
//    setActivityInitialTouchMode(false);
//  }
//
//  public void testNull()
//  {
//    final DynamicLayoutTestActivity activity = getActivity();
//    activity.finish();
//  }
//
//  public void testEspresso()
//  {
//    final DynamicLayoutTestActivity activity = getActivity();
//    onView(withText("Test 1")).check(matches(withText("Test 1")));
//    activity.finish();
//  }
}
