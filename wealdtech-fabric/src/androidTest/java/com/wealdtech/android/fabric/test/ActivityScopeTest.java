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

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Test activity-level scope
 */
public class ActivityScopeTest extends ActivityInstrumentationTestCase2<ActivityScopeTestActivity>
{
  public ActivityScopeTest()
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

    assertEquals(Fabric.getInstance().get("test integer", Integer.class), testGlobalInt);
    assertEquals(Fabric.getInstance().get(activity, "test integer", Integer.class), testActivityInt);

    activity.finish();
  }

  public void testPersistence()
  {
    final ActivityScopeTestActivity activity = getActivity();
    final Integer testInt = 53;
    Fabric.getInstance().set(activity, "test persisting integer", testInt);
    Fabric.getInstance().persist(activity, "test persisting integer");
    assertThat(testInt.equals(Fabric.getInstance().<Integer>get(activity, "test persisting integer", Integer.class)));
    activity.finish();

    final ActivityScopeTestActivity activity2 = getActivity();
    assertThat(testInt.equals(Fabric.getInstance().<Integer>get(activity, "test persisting integer", Integer.class)));
    activity2.finish();
  }
}
