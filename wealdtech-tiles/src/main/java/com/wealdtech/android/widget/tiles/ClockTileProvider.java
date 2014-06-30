package com.wealdtech.android.widget.tiles;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class ClockTileProvider implements TileProvider<Date>
{
  private final List<DataChangedListener> listeners = new ArrayList<>();

  private Date data;

  public Date getData()
  {
    return data;
  }

  @Override
  public void addDataChangedListener(final DataChangedListener listener)
  {
    listeners.add(listener);
  }

  private void notifyListeners()
  {
    for (final DataChangedListener listener : listeners)
    {
      listener.onDataChanged();
    }
  }

  public ClockTileProvider()
  {
    new AsyncTask<Void, Void, String>()
    {
      @Override
      protected String doInBackground(final Void... params)
      {
        while (true)
        {
          data = new Date();
          notifyListeners();
          try
          {
            Thread.sleep(1000L);
          }
          catch (final InterruptedException ignored)
          {
          }
        }
      }
    };
  }
}
