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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * The default alarm handler.  This creates a notification that redirects to the handler when clicks
 */
public class DefaultAlarmHandler extends AbstractAlarmHandler
{
  private static final String TAG = DefaultAlarmHandler.class.getCanonicalName();

  @Override
  public void onAlarm(final Context context, final Alarm alarm)
  {
    Log.d(TAG, "Alarm triggered: " + alarm);
    NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(context)
//            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("My notification")
            .setContentText("ID is " + alarm.getId().toString());
//    Intent resultIntent = new Intent(context, StatusOfChild.class);
//    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//    stackBuilder.addParentStack(StatusOfChild.class);
//    stackBuilder.addNextIntent(resultIntent);
//    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
    mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(), 0));
    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.notify(9011, mBuilder.build());
  }
}
