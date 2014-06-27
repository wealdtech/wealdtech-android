package com.wealdtech.test;

import android.test.ActivityInstrumentationTestCase2;

public class TileLayoutTest extends ActivityInstrumentationTestCase2<TileLayoutTestActivity>
{
  public TileLayoutTest()
  {
    super(TileLayoutTestActivity.class);
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    setActivityInitialTouchMode(false);
  }

  public void testNull()
  {
    final TileLayoutTestActivity activity = getActivity();
    activity.finish();
  }

//  public void testEspresso()
//  {
//    final TileLayoutTestActivity activity = getActivity();
//    onView(withText("Text view 1")).perform(click()).check(matches(withText("Text view 1")));
//    activity.finish();
//  }
}
