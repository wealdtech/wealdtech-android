package com.wealdtech.android.fabric.test;

import android.test.ActivityInstrumentationTestCase2;
import com.wealdtech.android.fabric.Fabric;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Test activity-level scope
 */
public class ComponentScopeTest extends ActivityInstrumentationTestCase2<ActivityScopeTestActivity>
{
  public ComponentScopeTest()
  {
    super(ActivityScopeTestActivity.class);
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    setActivityInitialTouchMode(false);
  }

  public void testActivityScope()
  {
    final ActivityScopeTestActivity activity = getActivity();

    // Global scope
    final Integer testGlobalInt = 1;
    Fabric.getInstance().set("test integer", testGlobalInt);

    // Activity scope
    final Integer testActivityInt = 2;
    Fabric.getInstance().set(activity, "test integer", testActivityInt);

    // Component scope
    final Integer testScopeInt = 3;
    Fabric.getInstance().set(activity, "test component", "test integer", testScopeInt);

    assertThat(Fabric.getInstance().get("test integer").equals(testGlobalInt));
    assertThat(Fabric.getInstance().get(activity, "test integer").equals(testActivityInt));
    assertThat(Fabric.getInstance().get(activity, "test component", "test integer").equals(testScopeInt));

    activity.finish();
  }
}
