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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.wealdtech.android.fabric.Fabric;

import java.util.HashMap;
import java.util.Map;

/**
 * Android alarms.
 * Alarms manages setting and cancelling of alarms.  It also persists alarms so that they are not lost if a reboot occurs
 */
public class Alarms
{
  protected static final String ALARMS_FABRIC_KEY = "com.wealdtech.alarms.alarms";

  protected static final String REQUEST_CODE = "requestcode";

  /**
   * Set an alarm
   * @param context the context
   * @param alarm an alarm
   * @return a previous {@link Alarm} if this alarm overwrote it; otherwise {@code null}
   */
  public static Alarm setAlarm(final Context context, final Alarm alarm)
  {
    final AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    final Intent intent = new Intent(context, AlarmService.class);
    intent.putExtra(REQUEST_CODE, alarm.getGroup());
    final PendingIntent pendingIntent = PendingIntent.getService(context, alarm.getGroup(), intent, 0);

    mgr.set(AlarmManager.RTC_WAKEUP, alarm.getTimestamp(), pendingIntent);

    // Add the alarm to our persistent store
    final Map<Integer, Alarm> alarms =
        MoreObjects.firstNonNull(Fabric.getInstance(context).get(ALARMS_FABRIC_KEY, new TypeReference<HashMap<Integer, Alarm>>() {}),
                                 Maps.<Integer, Alarm>newHashMap());
    final Alarm priorAlarm = alarms.put(alarm.getGroup(), alarm);
    Fabric.getInstance(context).set(ALARMS_FABRIC_KEY, alarms);

    return priorAlarm;
  }

  /**
   * Cancel an existing alarm
   * @param alarm the alarm to cancel
   * @return {@code true} if the alarm was found, otherwise {@code false}
   */
  public static boolean cancelAlarm(final Context context, final Alarm alarm)
  {
    // Remove the alarm from our persistent store
    final Map<Integer, Alarm> alarms =
        MoreObjects.firstNonNull(Fabric.getInstance(context).get(ALARMS_FABRIC_KEY, new TypeReference<HashMap<Integer, Alarm>>() {}),
                                 Maps.<Integer, Alarm>newHashMap());
    final Alarm priorAlarm = alarms.remove(alarm.getGroup());
    Fabric.getInstance(context).set(ALARMS_FABRIC_KEY, alarms);

    return priorAlarm != null;
  }
}
