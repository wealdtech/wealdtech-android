package com.wealdtech.android.providers;

import android.os.AsyncTask;
import com.wealdtech.android.tiles.DataChangedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A provider provides data.  The data provided can be accessed in one of two ways: <ul> <li>Directly, by calling {@code
 * getData()}</li> <li>When the data changes, by adding a {@code DataChangedListener} through {@code addDataChangedListener()}</li>
 * </ul>
 */
public abstract class AbstractProvider<T> implements Provider<T>
{
  private static final Logger LOG = LoggerFactory.getLogger(AbstractProvider.class);

  private final List<DataChangedListener<T>> listeners = new ArrayList<>();

  private T data;

  private boolean providing;

  private long updateInterval;

  public AbstractProvider(final long updateInterval)
  {
    this.updateInterval = updateInterval;
    providing = false;
  }

  public void startProviding()
  {
    if (updateInterval > 0)
    {
      providing = true;
      new AsyncTask<Void, T, Void>()
      {
        @Override
        protected Void doInBackground(final Void... params)
        {
          while (providing)
          {
            data = obtainData();
            publishProgress(data);
            try
            {
              Thread.sleep(updateInterval);
            }
            catch (final InterruptedException ignored)
            {
            }
          }
          return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onProgressUpdate(T... item)
        {
          notifyListeners();
        }
      }.execute(null, null, null);
    }
  }

  public void stopProviding()
  {
    providing = false;
  }

  public boolean isProviding()
  {
    return providing;
  }

  public abstract T obtainData();

  public T getData()
  {
    return data;
  }

  protected void setData(final T data)
  {
    this.data = data;
    notifyListeners();
  }

  @Override
  public void addDataChangedListener(final DataChangedListener<T> listener)
  {
    if (data == null)
    {
      // Hard-code a fetch if we don't have anything yet
      data = obtainData();
    }

    listeners.add(listener);
    // Give the listener a back reference to us.  This also triggers a refresh of the listener
    listener.setProvider(this);
  }

  protected void notifyListeners()
  {
    for (final DataChangedListener<T> listener : listeners)
    {
      listener.onDataChanged(data);
    }
  }
}
