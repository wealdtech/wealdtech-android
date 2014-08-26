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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.wealdtech.android.fabric.Fabric;

import java.util.Date;
import java.util.Map;

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

  public void testComplexType()
  {
    final GlobalScopeTestActivity activity = getActivity();

    final Map<String, Date> testMap = Maps.newHashMap();
    testMap.put("test 1", new Date(1000000000000L));
    testMap.put("test 2", new Date(2000000000000L));

    Fabric.getInstance().set("test map", testMap);
    assertThat(Fabric.getInstance().<Map<String, Date>>get("test map").equals(testMap));

    activity.finish();
  }
}
