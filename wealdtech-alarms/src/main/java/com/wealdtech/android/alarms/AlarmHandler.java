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

import android.content.Context;

/**
 * An interface to handle alarms
 */
public interface AlarmHandler
{
  /**
   * Called when an alarm is triggered
   * @param context the context
   * @param alarm the alarm that was triggered
   */
  void onAlarm(Context context, Alarm alarm);
}
