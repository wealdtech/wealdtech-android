/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.utils;

import android.os.Build;
import android.view.View;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public class ViewUtils
{
  private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

  public static int generateViewId()
  {
    if (Build.VERSION.SDK_INT < 17)
    {
      for (; ; )
      {
        final int result = sNextGeneratedId.get();
        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
        int newValue = result + 1;
        if (newValue > 0x00FFFFFF)
        {
          newValue = 1; // Roll over to 1, not 0.
        }
        if (sNextGeneratedId.compareAndSet(result, newValue))
        {
          return result;
        }
      }
    }
    else
    {
      return View.generateViewId();
    }
  }

  public static String dump(Object object)
  {
    Field[] fields = object.getClass().getDeclaredFields();
    StringBuilder sb = new StringBuilder();
    sb.append(object.getClass().getSimpleName()).append('{');

    boolean firstRound = true;

    for (final Field field : fields)
    {
      if (!firstRound)
      {
        sb.append(", ");
      }
      firstRound = false;
      field.setAccessible(true);
      try
      {
        final Object fieldObj = field.get(object);
        final String value;
        if (null == fieldObj)
        {
          value = "null";
        }
        else
        {
          value = fieldObj.toString();
        }
        sb.append(field.getName()).append('=').append('\'').append(value).append('\'');
      }
      catch (IllegalAccessException ignore)
      {
        //this should never happen
      }

    }

    sb.append('}');
    return sb.toString();
  }
}
