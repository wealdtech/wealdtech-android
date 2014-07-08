package com.wealdtech.android.test;

import android.test.ActivityInstrumentationTestCase2;
import com.wealdtech.android.providers.Provider;
import com.wealdtech.android.test.providers.DateProvider;

import java.util.Date;

public class VisibilityTest extends ActivityInstrumentationTestCase2<VisibilityTestActivity>
{
  private final Provider<Date> dateProvider;

  public VisibilityTest()
  {
    super(VisibilityTestActivity.class);
    dateProvider = new DateProvider();
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    setActivityInitialTouchMode(false);
  }

  public void testLayout()
  {
    final VisibilityTestActivity activity = getActivity();
//    dateProvider.addDataChangedListener(activity.holder.textTile);
    dateProvider.addDataChangedListener(activity.holder.flipTile);
    dateProvider.startProviding();

    try { Thread.sleep(20000L); } catch (final InterruptedException ignored) {}

    activity.finish();
  }
//  public void testNull()
//  {
//    final TileLayoutTestActivity activity = getActivity();
//    activity.finish();
//  }
//
//  public void testEspresso()
//  {
//    final TileLayoutTestActivity activity = getActivity();
////    onView(withId(R.id.test_layout_text_tile_1)).check(matches(withText("Text view 1")));
//    activity.finish();
//  }
}
