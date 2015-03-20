/*
 * Copyright 2012 - 2015 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.alarms;

import android.app.Activity;
import android.os.Bundle;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.wealdtech.WID;
import com.wealdtech.android.fabric.Fabric;
import com.wealdtech.android.fabric.persistence.PrefsPersistenceStore;
import org.joda.time.DateTime;

/**
 */
public class AlarmsTestActivity extends Activity
{
  static
  {
    // Only run this once regardless of how many times we start the activity
    BasicLogcatConfigurator.configureDefaultContext();
  }

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Fabric.init(new PrefsPersistenceStore(getApplicationContext()));
    Fabric.getInstance(getApplicationContext()).set("test 1", 1);
    Fabric.getInstance().set("test 2", 2);
    Alarms.setAlarm(this, Alarm.builder()
                               .id(WID.<Alarm>generate())
                               .requestCode(1)
                               .timestamp(new DateTime().getMillis() + 10000L)
                               .handler(TestAlarmHandler.class)
                               .build());
  }
}
