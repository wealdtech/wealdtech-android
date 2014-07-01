package com.wealdtech.android.widget.tiles;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class ClockTileProvider extends AbstractTileProvider<Date>
{
  public ClockTileProvider()
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
