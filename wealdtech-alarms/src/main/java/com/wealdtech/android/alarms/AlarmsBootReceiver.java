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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.wealdtech.android.fabric.Fabric;
import com.wealdtech.android.fabric.persistence.PrefsPersistenceStore;

import java.util.HashMap;
import java.util.Map;

/**
 * A receiver which is started on boot to reschedule existing alarms
 */
public class AlarmsBootReceiver extends BroadcastReceiver
{
  @Override
  public void onReceive(final Context context, final Intent intent)
  {
    Fabric.init(new PrefsPersistenceStore(context));
    // Fetch existing alarms and reinstate them
    final Map<Integer, Alarm> alarms =
        Fabric.getInstance().get(Alarms.ALARMS_FABRIC_KEY, new TypeReference<HashMap<Integer, Alarm>>() {});
    if (alarms != null)
    {
      Log.d(getClass().getCanonicalName(), "Reinstating alarms " + alarms.toString());
      for (final Alarm alarm : alarms.values())
      {
        Alarms.setAlarm(context, alarm);
      }
    }
  }
}

