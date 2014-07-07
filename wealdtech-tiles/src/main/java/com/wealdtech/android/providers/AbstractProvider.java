package com.wealdtech.android.providers;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * A provider obtains data and provides it to a number of listeners. provides data.  The data provided can be accessed in one of two
 * ways: <ul> <li>Directly, by calling {@code getData()}</li> <li>When the data changes, by adding a {@code DataChangedListener}
 * through {@code addDataChangedListener()}</li> </ul>
 */
public abstract class AbstractProvider<T> implements Provider<T>
{
  private static final Logger LOG = LoggerFactory.getLogger(AbstractProvider.class);

  private final List<DataChangedListener<T>> listeners = new ArrayList<>();

  private T data;

  private boolean providing;

  private long updateInterval;

  private AsyncTask<Void, T, Void> pollingTask;

  public AbstractProvider(final long updateInterval)
  {
    this.updateInterval = updateInterval;
    providing = false;
  }

  public void startProviding() throws IllegalStateException
  {
    if (updateInterval > 0)
    {
      startPolling();
    }
    else
    {
      fetchOnce();
    }
    providing = true;
  }

  private void startPolling()
  {
    pollingTask = new AsyncTask<Void, T, Void>()
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
      protected void onProgressUpdate(final T... item)
      {
        notifyListeners();
      }
    };
    pollingTask.execute(null, null, null);
  }

//  private void fetchOnce()
//  {
//    new AsyncTask<Void, T, Void>()
//    {
//      @Override
//      protected Void doInBackground(final Void... params)
//      {
//        data = obtainData();
//        publishProgress(data);
//        return null;
//      }
//
//      @SuppressWarnings("unchecked")
//      @Override
//      protected void onProgressUpdate(final T... item)
//      {
//        notifyListeners();
//      }
//    }.execute(null, null, null);
//  }

    private void fetchOnce()
    {
      new AsyncTask<Void, Void, T>()
      {
        @Override
        protected T doInBackground(final Void... params)
        {
          return obtainData();
        }
        @Override
        protected void onPostExecute(final T result)
        {
          data = result;
          notifyListeners();
        }
      }.execute(null, null, null);
    }

  public void stopProviding()
  {
    providing = false;
    if (pollingTask != null)
    {
      pollingTask.cancel(true);
      pollingTask = null;
    }
  }

  public boolean isProviding()
  {
    return providing;
  }

  public abstract T obtainData();

  public T getData() throws IllegalStateException
  {
    if (!providing)
    {
      throw new IllegalStateException("Attempt to obtain data from provider which is not providing");
    }
    return data;
  }

  /**
   * {@inheritDoc}
   * If this provider is currently providing then it will update the passed-in listener on the UI thread
   */
  @Override
  public void addDataChangedListener(@Nonnull final DataChangedListener<T> listener)
  {
    listeners.add(listener);
    if (providing)
    {
      new Handler(Looper.getMainLooper()).post(new Runnable()
      {
        @Override
        public void run()
        {
          listener.onDataChanged(data);
        }
      });
    }
  }

  @Override
  public void removeDataChangedListener(@Nonnull final DataChangedListener<T> listener)
  {
    listeners.remove(listener);
  }

  /**
   * Notify listeners that our data has changed.
   * Note that this always runs on the UI thread as it can update views
   */
  private void notifyListeners()
  {
    new Handler(Looper.getMainLooper()).post(new Runnable()
    {
      @Override
      public void run()
      {
        for (final DataChangedListener<T> listener : listeners)
        {
          listener.onDataChanged(data);
        }
      }
    });
  }
}