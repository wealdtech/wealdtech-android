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
import com.google.common.collect.Maps;
import com.wealdtech.WID;
import com.wealdtech.android.fabric.Fabric;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Test persistence functionality of Fabric
 */
public class PersistenceTest extends ActivityInstrumentationTestCase2<PersistenceTestActivity>
{
  public PersistenceTest()
  {
    super(PersistenceTestActivity.class);
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    setActivityInitialTouchMode(false);
  }

  public void testWID()
  {
    try
    {
      final PersistenceTestActivity activity = getActivity();

      final WID<Date> testWid = WID.generate();

      Fabric.getInstance().set("test wid", testWid);
      assertThat(Fabric.getInstance().<WID<Date>>get("test wid").equals(testWid));

      activity.finish();
    }
    finally
    {
      Fabric.getInstance().clear("test wid");
    }
  }

  public void testWIDMap()
  {
    try
    {
      final PersistenceTestActivity activity = getActivity();

      final Map<String, WID<Date>> testMap = Maps.newHashMap();
      testMap.put("test 1", WID.<Date>generate());
      testMap.put("test 2", WID.<Date>generate());

      Fabric.getInstance().set("test wid map", testMap);
      assertThat(Fabric.getInstance().<Map<String, WID<Date>>>get("test wid map").equals(testMap));

      activity.finish();
    }
    finally
    {
      Fabric.getInstance().clear("test wid map");
    }
  }

  public void testDifferentTypes()
  {
    try
    {
      final PersistenceTestActivity activity = getActivity();

      Fabric.getInstance().set("test date", new Date());
      Fabric.getInstance().set("test inet", new InetSocketAddress(10));

      assertThat(Fabric.getInstance().get("test date") instanceof Date);
      assertThat(Fabric.getInstance().get("test inet") instanceof InetSocketAddress);

      activity.finish();
    }
    finally
    {
      Fabric.getInstance().clear("test date");
      Fabric.getInstance().clear("test inet");
    }
  }
}
