package com.wealdtech.android.widget.tiles;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class ClockTileProvider implements TileProvider<Date>
{
  private static final Logger LOG = LoggerFactory.getLogger(ClockTileProvider.class);

  private final List<DataChangedListener> listeners = new ArrayList<>();

  private Date data;

  public Date getData()
  {
    return data;
  }

  @Override
  public void addDataChangedListener(final DataChangedListener listener)
  {
    LOG.info("Adding data changed listener");
    listeners.add(listener);
  }

  private void notifyListeners()
  {
    for (final DataChangedListener listener : listeners)
    {
      LOG.info("Notifying listener");
      listener.onDataChanged();
    }
  }

  public ClockTileProvider()
  {
    new AsyncTask<Void, Date, String>()
    {
      @Override
      protected String doInBackground(final Void... params)
      {
        while (true)
        {
          LOG.info("tick");
          data = new Date();
          publishProgress(data);
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
