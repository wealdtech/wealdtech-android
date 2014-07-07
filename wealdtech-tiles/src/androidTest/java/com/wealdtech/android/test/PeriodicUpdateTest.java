package com.wealdtech.android.test;

import android.test.ActivityInstrumentationTestCase2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeriodicUpdateTest extends ActivityInstrumentationTestCase2<PeriodicUpdateTestActivity>
{
  private static final Logger LOG = LoggerFactory.getLogger(PeriodicUpdateTest.class);

  public PeriodicUpdateTest()
  {
    super(PeriodicUpdateTestActivity.class);
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    setActivityInitialTouchMode(false);
  }

  // Ensure that we can create a provider, set a listener then start providing
  public void testPeriodicUpdate()
  {
   }
}
