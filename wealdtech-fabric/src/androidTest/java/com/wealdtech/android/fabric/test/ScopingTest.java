/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

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

    assertEquals(Fabric.getInstance().get(activity, "test component", "test integer", Integer.class), testComponentInt);
    Fabric.getInstance().clear(activity, "test component", "test integer");
    assertNull(Fabric.getInstance().get(activity, "test component", "test integer", Integer.class));

    assertEquals(Fabric.getInstance().get(activity, "test integer", Integer.class), testActivityInt);
    Fabric.getInstance().clear(activity, "test integer");
    assertNull(Fabric.getInstance().get(activity, "test integer", Integer.class));

    assertEquals(Fabric.getInstance().get("test integer", Integer.class), testGlobalInt);
    Fabric.getInstance().clear("test integer");
    assertNull(Fabric.getInstance().get("test integer", Integer.class));

    activity.finish();
  }
}
