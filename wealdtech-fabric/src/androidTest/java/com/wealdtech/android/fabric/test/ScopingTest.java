package com.wealdtech.android.fabric.test;

import android.test.ActivityInstrumentationTestCase2;
import com.wealdtech.android.fabric.Fabric;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Test scoping from component up to global
 */
public class ScopingTest extends ActivityInstrumentationTestCase2<ActivityScopeTestActivity>
{
  public ScopingTest()
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

    assertThat(Fabric.getInstance().get(activity, "test component", "test integer").equals(testScopeInt));

    Fabric.getInstance().clear(activity, "test component", "test integer");
    assertThat(Fabric.getInstance().get(activity, "test component", "test integer").equals(testActivityInt));

    Fabric.getInstance().clear(activity, "test integer");
    assertThat(Fabric.getInstance().get(activity, "test component", "test integer").equals(testGlobalInt));

    Fabric.getInstance().clear("test integer");
    assertNull(Fabric.getInstance().get(activity, "test component", "test integer"));

    activity.finish();
  }
}
