/*
 * Copyright 2012 - 2016 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import com.google.common.collect.Lists;
import com.wealdtech.android.ears.services.R;

import java.util.List;

/**
 * Abstract Ears service, providing the infrastructure for starting and stopping the service and running callbacks
 */
public abstract class AbstractEarsService extends Service implements EarsService, SpeechActivationListener
{
  private final List<EarsCallback> callbacks;
  protected boolean listening;

  public AbstractEarsService()
  {
    callbacks = Lists.newArrayList();
    listening = false;
  }

  private static final String TAG = "SpeechActivationService";
  public static final String NOTIFICATION_ICON_RESOURCE_INTENT_KEY =
      "NOTIFICATION_ICON_RESOURCE_INTENT_KEY";
  public static final String ACTIVATION_TYPE_INTENT_KEY =
          "ACTIVATION_TYPE_INTENT_KEY";
  public static final String ACTIVATION_RESULT_INTENT_KEY =
          "ACTIVATION_RESULT_INTENT_KEY";
  public static final String ACTIVATION_RESULT_BROADCAST_NAME =
          "root.gast.playground.speech.ACTIVATION";

  /**
   * send this when external code wants the Service to stop
   */
  public static final String ACTIVATION_STOP_INTENT_KEY =
          "ACTIVATION_STOP_INTENT_KEY";

  public static final int NOTIFICATION_ID = 10298;

  private boolean isStarted;

  private SpeechActivator activator;

  @Override
  public void onCreate()
  {
      super.onCreate();
      isStarted = false;
  }

  public static Intent makeStartServiceIntent(Context context,
          String activationType)
  {
      Intent i = new Intent(context, SpeechActivationService.class);
      i.putExtra(ACTIVATION_TYPE_INTENT_KEY, activationType);
      return i;
  }

  public static Intent makeServiceStopIntent(Context context)
  {
      Intent i = new Intent(context, SpeechActivationService.class);
      i.putExtra(ACTIVATION_STOP_INTENT_KEY, true);
      return i;
  }
  @Override
      public int onStartCommand(Intent intent, int flags, int startId)
      {
          if (intent != null)
          {
              if (intent.hasExtra(ACTIVATION_STOP_INTENT_KEY))
              {
                  Log.d(TAG, "stop service intent");
                  activated(false);
              }
              else
              {
                  if (isStarted)
                  {
                      // the activator is currently started
                      // if the intent is requesting a new activator
                      // stop the current activator and start
                      // the new one
                      if (isDifferentType(intent))
                      {
                          Log.d(TAG, "is different type");
                          stopActivator();
                          startDetecting(intent);
                      }
                      else
                      {
                          Log.d(TAG, "already started this type");
                      }
                  }
                  else
                  {
                      // activator not started, start it
                      startDetecting(intent);
                  }
              }
          }

          // restart in case the Service gets canceled
          return START_REDELIVER_INTENT;
      }
  private void startDetecting(Intent intent)
      {
          Log.d(TAG, "extras: " + intent.getExtras().toString());
          if (activator == null)
          {
              Log.d(TAG, "null activator");
          }

          activator = getRequestedActivator(intent);
          Log.d(TAG, "started: " + activator.getClass().getSimpleName());
          isStarted = true;
          activator.detectActivation();
          startForeground(NOTIFICATION_ID, getNotification(intent));
      }

      private SpeechActivator getRequestedActivator(Intent intent)
      {
          String type = intent.getStringExtra(ACTIVATION_TYPE_INTENT_KEY);
          // create based on a type name
          SpeechActivator speechActivator =
                  SpeechActivatorFactory.createSpeechActivator(this, this, type);
          return speechActivator;
      }

      /**
       * determine if the intent contains an activator type
       * that is different than the currently running type
       */
      private boolean isDifferentType(Intent intent)
      {
          boolean different = false;
          if (activator == null)
          {
              return true;
          }
          else
          {
              SpeechActivator possibleOther = getRequestedActivator(intent);
              different = !(possibleOther.getClass().getName().
                      equals(activator.getClass().getName()));
          }
          return different;
      }

      @Override
      public void activated(boolean success)
      {
          // make sure the activator is stopped before doing anything else
          stopActivator();

          // broadcast result
          Intent intent = new Intent(ACTIVATION_RESULT_BROADCAST_NAME);
          intent.putExtra(ACTIVATION_RESULT_INTENT_KEY, success);
          sendBroadcast(intent);

          // always stop after receive an activation
          stopSelf();
      }

      @Override
      public void onDestroy()
      {
          Log.d(TAG, "On destroy");
          super.onDestroy();
          stopActivator();
          stopForeground(true);
      }

      private void stopActivator()
      {
          if (activator != null)
          {
              Log.d(TAG, "stopped: " + activator.getClass().getSimpleName());
              activator.stop();
              isStarted = false;
          }
      }

      @SuppressLint("NewApi")
      private Notification getNotification(Intent intent)
      {
          // determine label based on the class
          String name = SpeechActivatorFactory.getLabel(this, activator);
          String message =
                  getString(R.string.speech_activation_notification_listening)
                          + " " + name;
          String title = getString(R.string.speech_activation_notification_title);

          PendingIntent pi =
                  PendingIntent.getService(this, 0, makeServiceStopIntent(this),
                          0);

          int icon = intent.getIntExtra(NOTIFICATION_ICON_RESOURCE_INTENT_KEY, R.drawable.icon);

          Notification notification;
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
          {
              Notification.Builder builder = new Notification.Builder(this);
              builder.setSmallIcon(icon)
                      .setWhen(System.currentTimeMillis()).setTicker(message)
                      .setContentTitle(title).setContentText(message)
                      .setContentIntent(pi);
              notification = builder.getNotification();
          }
          else
          {
            notification = new Notification.Builder(this).setSmallIcon(icon)
                                                         .setContentTitle(title)
                                                         .setContentText(message)
                                                         .setContentIntent(pi)
                                                         .build();
          }

          return notification;
      }

      @Override
      public IBinder onBind(Intent intent)
      {
          return null;
      }@Override
  public void startListening(final String prompt)
  {
    this.listening = true;
  }

  @Override
  public void stopListening()
  {
    this.listening = false;
  }

  @Override
  public void addCallback(final EarsCallback callback)
  {
    callbacks.add(callback);
  }

  @Override
  public void removeCallback(final EarsCallback callback)
  {
    callbacks.remove(callback);
  }

  public boolean isListener()
  {
    return listening;
  }
}