Notifications
=============

What Is It?
-----------

A library which simplifies handling notifications in Android.

What Does It Do?
----------------

Notifications provides context-sensitive management of notifications.

How Do I Use It?
----------------

Notifications.disableNotifications(context)
Notifications.enableNotifications(context)
Notifications.clearNotifications(context)
Notifications.addNotification(context, notification)
Notifications.removeNotification(context, notificationId)

Notification:
  id
  sound
  vibrate
  etc.


First off you need to create an alarm handler.  An alarm handler handles the alarm when it is triggered.  Alarm handlers should extend `AbstractAlarmHandler` and implement the `onAlarm` method.  Here is a simple example:

public class LogAlarmHandler extends AbstractAlarmHandler
{
  @Override
  public void onAlarm(final Context context, final Alarm alarm)
  {
    Log.i("Alarm", "An alarm was triggered");
  }
}

Next you need to create an alarm.  A minimal builder for an alarm is as follows:

    final Alarm myAlarm = Alarm.builder()
                               .group(group)
                               .timestamp(new DateTime().plusHours(1).getMillis()) // 1 hour from now
                               .handler(handler)
                               .build();

Where `group` is an integer that defines the alarm group and `timestamp` is a long that defines the absolute time in milliseconds when the alarm should trigger.  `handler` is the alarm handler as described above.

The purpose of alarm groups is to allow simple overwriting of alarms.  There can only be one alarm set in any alarm group at any time.  Attempting to set more than one alarm in the same alarm group will cause the prior alarm to be replaced.

To set an alarm you use the call `Alarms.setAlarm(context, alarm)` and if you want to clear the alarm then you can call `Alarms.clearAlarm(context, alarm)`.

And that's it.

If you want to add more data to your alarm then you simply use the `data` call in the builder, for example

    final Alarm myAlarm = Alarm.builder()
                               .group(1)
                               .timestamp(new DateTime().plusHours(1).getMillis()) // 1 hour from now
                               .handler(handler)
                               .data("my number", 1)
                               .data("my string", "foo")
                               .data("my object", obj)
                               .build();

and in your alarm handler you can fetch the data with, for example `alarm.get("my number", Integer.class)`.  Be aware that the additional data supplied will be persisted so that alarms can be recreated on reboot.  As such you should not store any objects which are very large or rely on non-persistent state.
