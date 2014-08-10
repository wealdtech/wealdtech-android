package com.wealdtech.android.fabric.test;

import android.test.ActivityInstrumentationTestCase2;
import com.google.common.collect.ImmutableSet;
import com.wealdtech.android.fabric.Fabric;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Test global scope functionality of Fabric
 */
public class GlobalScopeTest extends ActivityInstrumentationTestCase2<GlobalScopeTestActivity>
{
  public GlobalScopeTest()
  {
    super(GlobalScopeTestActivity.class);
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    setActivityInitialTouchMode(false);
  }

  public void testSetAndGet()
  {
    final GlobalScopeTestActivity activity = getActivity();

    final Integer testInt = 1;
    Fabric.getInstance().set("test integer", testInt);
    assertThat(Fabric.getInstance().<Integer>get("test integer").equals(testInt));

    final ImmutableSet<Integer> testSet = ImmutableSet.of(1, 2, 3);
    Fabric.getInstance().set("test set", testSet);
    assertThat(Fabric.getInstance().get("test set").equals(testSet));

    activity.finish();
  }
}
