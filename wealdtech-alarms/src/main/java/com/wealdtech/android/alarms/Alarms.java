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
import android.util.Log;
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
  private static final String TAG = Alarms.class.getCanonicalName();

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
    // Always use the application context
    final Context appContext = context.getApplicationContext();
    Log.d(TAG, "setAlarm: Application context is " + appContext.getApplicationInfo().className);
    final AlarmManager mgr = (AlarmManager)appContext.getSystemService(Context.ALARM_SERVICE);
    final Intent intent = new Intent(appContext, AlarmService.class);
    intent.putExtra(REQUEST_CODE, alarm.getGroup());
    final PendingIntent pendingIntent = PendingIntent.getService(appContext, alarm.getGroup(), intent, 0);

    // Add the alarm to our persistent store
    final Map<Integer, Alarm> alarms =
        MoreObjects.firstNonNull(Fabric.getInstance(appContext).get(ALARMS_FABRIC_KEY, new TypeReference<HashMap<Integer, Alarm>>() {}),
                                 Maps.<Integer, Alarm>newHashMap());
    Log.d(TAG, "setAlarm: Alarms were " + alarms);
    final Alarm priorAlarm = alarms.put(alarm.getGroup(), alarm);
    Fabric.getInstance(appContext).set(ALARMS_FABRIC_KEY, alarms);
    Log.d(TAG, "setAlarm: Alarms are now " +
               Fabric.getInstance(appContext).get(ALARMS_FABRIC_KEY, new TypeReference<HashMap<Integer, Alarm>>() {}));

    // Kick off the alarm
    mgr.set(AlarmManager.RTC_WAKEUP, alarm.getTimestamp(), pendingIntent);

    return priorAlarm;
  }

  /**
   * Cancel an existing alarm
   * @param context the context
   * @param alarm the alarm to cancel
   * @return {@code true} if the alarm was found, otherwise {@code false}
   */
  public static boolean cancelAlarm(final Context context, final Alarm alarm)
  {
    // Always use the application context
    final Context appContext = context.getApplicationContext();
    Log.d(TAG, "cancelAlarm: Application context is " + appContext.getApplicationInfo().className);

    // Remove the alarm from our persistent store
    final Map<Integer, Alarm> alarms =
        MoreObjects.firstNonNull(Fabric.getInstance(appContext).get(ALARMS_FABRIC_KEY, new TypeReference<HashMap<Integer, Alarm>>() {}),
                                 Maps.<Integer, Alarm>newHashMap());
    Log.d(TAG, "cancelAlarm: Alarms were " + alarms);
    final Alarm priorAlarm = alarms.remove(alarm.getGroup());
    Fabric.getInstance(appContext).set(ALARMS_FABRIC_KEY, alarms);
    Log.d(TAG, "cancelAlarm: Alarms are now " + Fabric.getInstance(appContext).get(ALARMS_FABRIC_KEY, new TypeReference<HashMap<Integer, Alarm>>() {}));

    return priorAlarm != null;
  }
}
