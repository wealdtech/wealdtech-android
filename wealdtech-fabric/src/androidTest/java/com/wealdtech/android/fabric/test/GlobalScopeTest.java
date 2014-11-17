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

  public void testJDoc()
  {
    final String f = "{\"cognis.build\":3121,\"cognis.store.credentials\":[\"is.cogn.client.auth.PasswordCredentials\",{\"id\":\"sci@cogn.is\",\"token\":\"WQizu/Utsrk=\\n\",\"type\":\"Password\"}],\"cognis.store.last_sync_since.53e9218d5800081.53e9219034001ca\":[\"org.joda.time.DateTime\",\"2014-11-17T16:11:03+01:00 Europe/Berlin\"],\"cognis.store.identities\":[\"com.google.common.collect.SingletonImmutableList\",[[\"is.cogn.client.model.Identity\",{\"primary\":true,\"id\":\"53e9218d5800081\",\"name\":\"Single calendar instance\",\"scope\":\"Full\",\"primarycalendarid\":\"53e9219034001ca\",\"primaryusergroupid\":\"53e9218ffc001c9\",\"profile\":{\"hometimezone\":\"Europe/London\",\"locale\":\"en_GB\",\"calendarspecs\":[[\"java.util.LinkedHashMap\",{\"id\":\"549ee278a88837c\",\"name\":\"All\",\"primary\":\"false\",\"identityid\":\"53e9218d5800081\",\"calendarids\":[\"java.util.ArrayList\",[\"549ec96358000fb\",\"53e9219034001ca\"]],\"foldedtypes\":[\"java.util.ArrayList\",[\"Accommodation\",\"Anniversary\",\"Banner\",\"Vacation\",\"Holiday\"]],\"nointerest\":\"Hide\",\"nothappening\":\"Hide\",\"notattending\":\"Hide\"}],[\"java.util.LinkedHashMap\",{\"id\":\"549edbc461ba122\",\"name\":\"Test 2\",\"primary\":\"false\",\"identityid\":\"549ec960bc00085\",\"calendarids\":[\"java.util.ArrayList\",[\"549ec96358000fb\"]],\"foldedtypes\":[\"java.util.ArrayList\",[\"Accommodation\",\"Anniversary\",\"Banner\",\"Vacation\",\"Holiday\"]],\"nointerest\":\"Hide\",\"nothappening\":\"Hide\",\"notattending\":\"Hide\"}],[\"java.util.LinkedHashMap\",{\"id\":\"549ee278a9ea3ac\",\"name\":\"Single calendar instance\",\"primary\":\"true\",\"identityid\":\"53e9218d5800081\",\"calendarids\":[\"java.util.ArrayList\",[\"53e9219034001ca\"]],\"foldedtypes\":[\"java.util.ArrayList\",[\"Accommodation\",\"Anniversary\",\"Banner\",\"Vacation\",\"Holiday\"]],\"nointerest\":\"Hide\",\"nothappening\":\"Hide\",\"notattending\":\"Hide\"}]]}}]]],\"cognis.store.last_agenda_sync_since.549ee278a9ea3ac\":[\"org.joda.time.DateTime\",\"2014-11-17T15:11:02+00:00 UTC\"],\"cognis.store.calendar_specs\":[\"com.google.common.collect.RegularImmutableList\",[[\"is.cogn.profiles.CalendarSpec\",{\"id\":\"549ee278a9ea3ac\",\"name\":\"Single calendar instance\",\"primary\":true,\"identityid\":\"53e9218d5800081\",\"calendarids\":[\"com.google.common.collect.SingletonImmutableSet\",[\"53e9219034001ca\"]],\"foldedtypes\":[\"com.google.common.collect.RegularImmutableSet\",[\"Accommodation\",\"Anniversary\",\"Banner\",\"Vacation\",\"Holiday\"]],\"nointerest\":\"Hide\",\"nothappening\":\"Hide\",\"notattending\":\"Hide\"}],[\"is.cogn.profiles.CalendarSpec\",{\"id\":\"549ee278a88837c\",\"name\":\"All\",\"primary\":false,\"identityid\":\"53e9218d5800081\",\"calendarids\":[\"com.google.common.collect.RegularImmutableSet\",[\"549ec96358000fb\",\"53e9219034001ca\"]],\"foldedtypes\":[\"com.google.common.collect.RegularImmutableSet\",[\"Accommodation\",\"Anniversary\",\"Banner\",\"Vacation\",\"Holiday\"]],\"nointerest\":\"Hide\",\"nothappening\":\"Hide\",\"notattending\":\"Hide\"}],[\"is.cogn.profiles.CalendarSpec\",{\"id\":\"549edbc461ba122\",\"name\":\"Test 2\",\"primary\":false,\"identityid\":\"549ec960bc00085\",\"calendarids\":[\"com.google.common.collect.SingletonImmutableSet\",[\"549ec96358000fb\"]],\"foldedtypes\":[\"com.google.common.collect.RegularImmutableSet\",[\"Accommodation\",\"Anniversary\",\"Banner\",\"Vacation\",\"Holiday\"]],\"nointerest\":\"Hide\",\"nothappening\":\"Hide\",\"notattending\":\"Hide\"}]]],\"cognis.store.last_agenda_sync_range.549ee278a9ea3ac\":[\"com.google.common.collect.Range\",\"[2014-08-01T00:00:00+01:00 Europe/London,2016-12-01T00:00:00+00:00 Europe/London)\"],\"cognis.store.last_sync_range.53e9218d5800081.53e9219034001ca\":[\"com.google.common.collect.Range\",\"[2014-08-01T00:00:00+01:00 Europe/London,2016-12-01T00:00:00+00:00 Europe/London)\"]}";
  }
//  {"cognis.build":3121,"cognis.store.credentials":["is.cogn.client.auth.PasswordCredentials",{"id":"sci@cogn.is","token":"WQizu/Utsrk=\n","type":"Password"}],"cognis.store.last_sync_since.53e9218d5800081.53e9219034001ca":["org.joda.time.DateTime","2014-11-17T16:11:03+01:00 Europe/Berlin"],"cognis.store.identities":["com.google.common.collect.SingletonImmutableList",[["is.cogn.client.model.Identity",{"primary":true,"id":"53e9218d5800081","name":"Single calendar instance","scope":"Full","primarycalendarid":"53e9219034001ca","primaryusergroupid":"53e9218ffc001c9","profile":{"hometimezone":"Europe/London","locale":"en_GB","calendarspecs":[["java.util.LinkedHashMap",{"id":"549ee278a88837c","name":"All","primary":"false","identityid":"53e9218d5800081","calendarids":["java.util.ArrayList",["549ec96358000fb","53e9219034001ca"]],"foldedtypes":["java.util.ArrayList",["Accommodation","Anniversary","Banner","Vacation","Holiday"]],"nointerest":"Hide","nothappening":"Hide","notattending":"Hide"}],["java.util.LinkedHashMap",{"id":"549edbc461ba122","name":"Test 2","primary":"false","identityid":"549ec960bc00085","calendarids":["java.util.ArrayList",["549ec96358000fb"]],"foldedtypes":["java.util.ArrayList",["Accommodation","Anniversary","Banner","Vacation","Holiday"]],"nointerest":"Hide","nothappening":"Hide","notattending":"Hide"}],["java.util.LinkedHashMap",{"id":"549ee278a9ea3ac","name":"Single calendar instance","primary":"true","identityid":"53e9218d5800081","calendarids":["java.util.ArrayList",["53e9219034001ca"]],"foldedtypes":["java.util.ArrayList",["Accommodation","Anniversary","Banner","Vacation","Holiday"]],"nointerest":"Hide","nothappening":"Hide","notattending":"Hide"}]]}}]]],"cognis.store.last_agenda_sync_since.549ee278a9ea3ac":["org.joda.time.DateTime","2014-11-17T15:11:02+00:00 UTC"],"cognis.store.calendar_specs":["com.google.common.collect.RegularImmutableList",[["is.cogn.profiles.CalendarSpec",{"id":"549ee278a9ea3ac","name":"Single calendar instance","primary":true,"identityid":"53e9218d5800081","calendarids":["com.google.common.collect.SingletonImmutableSet",["53e9219034001ca"]],"foldedtypes":["com.google.common.collect.RegularImmutableSet",["Accommodation","Anniversary","Banner","Vacation","Holiday"]],"nointerest":"Hide","nothappening":"Hide","notattending":"Hide"}],["is.cogn.profiles.CalendarSpec",{"id":"549ee278a88837c","name":"All","primary":false,"identityid":"53e9218d5800081","calendarids":["com.google.common.collect.RegularImmutableSet",["549ec96358000fb","53e9219034001ca"]],"foldedtypes":["com.google.common.collect.RegularImmutableSet",["Accommodation","Anniversary","Banner","Vacation","Holiday"]],"nointerest":"Hide","nothappening":"Hide","notattending":"Hide"}],["is.cogn.profiles.CalendarSpec",{"id":"549edbc461ba122","name":"Test 2","primary":false,"identityid":"549ec960bc00085","calendarids":["com.google.common.collect.SingletonImmutableSet",["549ec96358000fb"]],"foldedtypes":["com.google.common.collect.RegularImmutableSet",["Accommodation","Anniversary","Banner","Vacation","Holiday"]],"nointerest":"Hide","nothappening":"Hide","notattending":"Hide"}]]],"cognis.store.last_agenda_sync_range.549ee278a9ea3ac":["com.google.common.collect.Range","[2014-08-01T00:00:00+01:00 Europe/London,2016-12-01T00:00:00+00:00 Europe/London)"],"cognis.store.last_sync_range.53e9218d5800081.53e9219034001ca":["com.google.common.collect.Range","[2014-08-01T00:00:00+01:00 Europe/London,2016-12-01T00:00:00+00:00 Europe/London)"]}
}
