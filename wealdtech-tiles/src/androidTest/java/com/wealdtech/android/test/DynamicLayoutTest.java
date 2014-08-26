/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.test;

import android.test.ActivityInstrumentationTestCase2;

public class DynamicLayoutTest extends ActivityInstrumentationTestCase2<DynamicLayoutTestActivity>
{
  public DynamicLayoutTest()
  {
    super(DynamicLayoutTestActivity.class);
  }

//  @Override
//  protected void setUp() throws Exception
//  {
//    super.setUp();
//    setActivityInitialTouchMode(false);
//  }
//
//  public void testNull()
//  {
//    final DynamicLayoutTestActivity activity = getActivity();
//    activity.finish();
//  }
//
//  public void testEspresso()
//  {
//    final DynamicLayoutTestActivity activity = getActivity();
//    onView(withText("Test 1")).check(matches(withText("Test 1")));
//    activity.finish();
//  }
}
