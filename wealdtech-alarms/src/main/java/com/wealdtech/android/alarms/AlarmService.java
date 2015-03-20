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

import android.app.IntentService;
import android.content.Intent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.wealdtech.android.fabric.Fabric;

import java.util.HashMap;
import java.util.Map;

/**
 * Service to handle Android alarms
 */
public class AlarmService extends IntentService
{
  public AlarmService()
  {
    super("Alarms");
  }

  @Override
  protected void onHandleIntent(final Intent intent)
  {
    final Integer requestCode = intent.getExtras().getInt(Alarms.REQUEST_CODE);
    final Map<Integer, Alarm> alarms = MoreObjects.firstNonNull(Fabric.getInstance(getApplicationContext())
                                                                      .get(Alarms.ALARMS_FABRIC_KEY,
                                                                           new TypeReference<HashMap<Integer, Alarm>>() {}),
                                                                Maps.<Integer, Alarm>newHashMap());
    final Alarm alarm = alarms.get(requestCode);
    // We remove the alarm before processing it so that the handler can call setAlarm() if it desires
    Alarms.cancelAlarm(getApplicationContext(), alarm);

    alarm.getHandler().onAlarm(getApplicationContext(), alarm);
  }
}
