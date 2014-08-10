package com.wealdtech.android.fabric.test;

import android.test.ActivityInstrumentationTestCase2;
import com.wealdtech.android.fabric.Fabric;

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

  public void testScoping()
  {
    final ActivityScopeTestActivity activity = getActivity();

    // Global scope
    final Integer testGlobalInt = 1;
    Fabric.getInstance().set("test integer", testGlobalInt);

    // Activity scope
    final Integer testActivityInt = 2;
    Fabric.getInstance().set(activity, "test integer", testActivityInt);

    // Component scope
    final Integer testComponentInt = 3;
    Fabric.getInstance().set(activity, "test component", "test integer", testComponentInt);

    assertEquals(Fabric.getInstance().get(activity, "test component", "test integer"), testComponentInt);
    Fabric.getInstance().clear(activity, "test component", "test integer");
    assertNull(Fabric.getInstance().get(activity, "test component", "test integer"));

    assertEquals(Fabric.getInstance().get(activity, "test integer"), testActivityInt);
    Fabric.getInstance().clear(activity, "test integer");
    assertNull(Fabric.getInstance().get(activity, "test integer"));

    assertEquals(Fabric.getInstance().get("test integer"), testGlobalInt);
    Fabric.getInstance().clear("test integer");
    assertNull(Fabric.getInstance().get("test integer"));

    activity.finish();
  }
}
