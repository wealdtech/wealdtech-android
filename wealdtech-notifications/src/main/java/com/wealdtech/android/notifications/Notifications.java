/*
 * Copyright 2012 - 2015 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.wealdtech.WID;
import com.wealdtech.android.fabric.Fabric;

import java.util.Map;

/**
 * Android notifications.
 * Manages notifications: additions, deletions and updates
 */
public class Notifications
{
  private static final String TAG = Notifications.class.getCanonicalName();

  protected static final String NOTIFICATIONS_FABRIC_ENABLED = "com.wealdtech.notifications.enabled";
  protected static final String NOTIFICATIONS_FABRIC_KEY = "com.wealdtech.notifications.notifications";

  private static final Map<String,NotificationTemplate> TEMPLATES = Maps.newHashMap();

  /**
   * Register a template
   */
  public static void registerTemplate(final String group, final NotificationTemplate template)
  {
    TEMPLATES.put(group, template);
  }

  /**
   * Add a notification
   */
  public static void addNotification(final Context context, final NotificationInfo notificationInfo)
  {
    // Ensure we use the application context
    final Context appContext = context.getApplicationContext();

    final Boolean enabled = MoreObjects.firstNonNull(Fabric.getInstance(appContext).get(NOTIFICATIONS_FABRIC_ENABLED, Boolean.class), true);
    if (enabled)
    {
//      // Find our existing notifications for this group
//      final LinkedListMultimap<String, NotificationInfo> notifications;
//      notifications = MoreObjects.firstNonNull(Fabric.getInstance(appContext)
//                                                     .get(NOTIFICATIONS_FABRIC_KEY,
//                                                          new TypeReference<LinkedListMultimap<String, NotificationInfo>>() {}),
//                                               LinkedListMultimap.<String, NotificationInfo>create());
//      final List<NotificationInfo> current = MoreObjects.firstNonNull(notifications.get(notificationInfo.getGroup()),
//                                                                      Lists.<NotificationInfo>newArrayList());
      // Obtain the template for the notification
      final NotificationTemplate template = MoreObjects.firstNonNull(TEMPLATES.get(notificationInfo.getGroup()), null);
      Log.e(TAG, "Using template " + template);
      final Notification notification = template.generate(appContext, notificationInfo);

//      // Current will be updated so save it
//      notifications.removeAll(notificationInfo.getGroup());
//      notifications.putAll(notificationInfo.getGroup(), current);
//      Fabric.getInstance(appContext).set(NOTIFICATIONS_FABRIC_KEY, notifications);

      if (notification != null)
      {
        final NotificationManager notificationManager = (NotificationManager)appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // FIXME need group-specific value.  Store with Fabric
        notificationManager.notify(1, notification);
      }
    }
  }

  /**
   * Remove a notification
   */
  public static void removeNotification(final Context context, final WID<NotificationInfo> notificationInfoId)
  {
    // Ensure we use the application context
    final Context appContext = context.getApplicationContext();
  }

  /**
   * Remove all notifications for a group
   */
  public static void removeNotificationGroup(final Context context, final String group)
  {
    // Ensure we use the application context
    final Context appContext = context.getApplicationContext();
  }

  /**
   * Disable notifications so that they do not trigger.
   * Note that when notifications are disabled addNotification() will still register the notification and retain details of it.
   */
  public static void disableNotifications(final Context context)
  {
    // Ensure we use the application context
    final Context appContext = context.getApplicationContext();

    // Obtain the current state
    final Boolean enabled = MoreObjects.firstNonNull(Fabric.getInstance(appContext).get(NOTIFICATIONS_FABRIC_ENABLED, Boolean.class), true);

    if (enabled)
    {
      Fabric.getInstance(appContext).set(NOTIFICATIONS_FABRIC_ENABLED, false);
    }
  }

  /**
   * Enable notifications so that they do not trigger.
   * Notifications are enabled by default
   */
  public static void enableNotifications(final Context context)
  {
    // Ensure we use the application context
    final Context appContext = context.getApplicationContext();

    // Obtain the current state
    final Boolean enabled = MoreObjects.firstNonNull(Fabric.getInstance(appContext).get(NOTIFICATIONS_FABRIC_ENABLED, Boolean.class), true);

    if (!enabled)
    {
      Fabric.getInstance(appContext).set(NOTIFICATIONS_FABRIC_ENABLED, true);
    }
  }

  /**
   * Remove all notifications
   * @param context
   */
  public static void removeNotifications(final Context context)
  {
    // Ensure we use the application context
    final Context appContext = context.getApplicationContext();

    final NotificationManager notificationManager = (NotificationManager)appContext.getSystemService(Context.NOTIFICATION_SERVICE);
    // FIXME need group-specific value.  Store with Fabric
    notificationManager.cancel(1);
  }
}
