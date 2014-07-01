package com.wealdtech.android.providers;

import android.os.AsyncTask;

import java.util.Date;

/**
 * Provide the date, updating every second
 */
public class DateProvider extends AbstractProvider<Date>
{
  public DateProvider()
  {
    new AsyncTask<Void, Date, String>()
    {
      @Override
      protected String doInBackground(final Void... params)
      {
        while (true)
        {
          setData(new Date());
          publishProgress(getData());
          try
          {
            Thread.sleep(1000L);
          }
          catch (final InterruptedException ignored)
          {
          }
        }
      }

      @SuppressWarnings("unchecked")
      @Override
      protected void onProgressUpdate(Date... item)
      {
        notifyListeners();
      }
    }.execute(null, null, null);
  }
}
