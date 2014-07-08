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
