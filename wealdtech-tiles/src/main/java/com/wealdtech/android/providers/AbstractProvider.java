package com.wealdtech.android.providers;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

  private AsyncTask<Void, Void, Void> pollingTask;

  public AbstractProvider(final long updateInterval)
  {
    this.updateInterval = updateInterval;
    providing = false;
  }

  public final void startProviding() throws IllegalStateException
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
    if (pollingTask != null)
    {
      pollingTask.cancel(true);
      pollingTask = null;
    }

    pollingTask = new AsyncTask<Void, Void, Void>()
    {
      @Override
      protected Void doInBackground(final Void... params)
      {
        while (!isCancelled())
        {
          final T newData = obtainData();
          final T oldData = data;
          data = newData;
          if (dataDifferent(oldData, newData))
          {
            publishProgress();
          }
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

      @Override
      protected void onProgressUpdate(final Void... item)
      {
        notifyListeners();
      }
    };
    // Run in thread pool for real multi-threading
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
    {
      pollingTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, null, null, null);
    }
    else
    {
      pollingTask.execute(null, null, null);
    }
  }

  private void fetchOnce()
  {
    final AsyncTask<Void, Void, T> task = new AsyncTask<Void, Void, T>()
    {
      @Override
      protected T doInBackground(final Void... params)
      {
        return obtainData();
      }

      @Override
      protected void onPostExecute(final T result)
      {
        final T oldData = data;
        data = result;
        if (dataDifferent(oldData, result))
        {
          notifyListeners();
        }
      }
    };
    // Run in thread pool for real multi-threading
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
    {
      task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, null, null, null);
    }
    else
    {
      task.execute(null, null, null);
    }
  }

  public final void stopProviding()
  {
    providing = false;
    if (pollingTask != null)
    {
      final boolean result = pollingTask.cancel(true);
      pollingTask = null;
    }
  }

  public final boolean isProviding()
  {
    return providing;
  }

  public abstract T obtainData();

  public final T getData() throws IllegalStateException
  {
    if (!providing)
    {
      throw new IllegalStateException("Attempt to obtain data from provider which is not providing");
    }
    return data;
  }

  /**
   * {@inheritDoc} If this provider is currently providing then it will update the passed-in listener immediately
   */
  @Override
  public final void addDataChangedListener(@Nonnull final DataChangedListener<T> listener)
  {
    listeners.add(listener);
    if (providing)
    {
      notifyListener(listener);
    }
  }

  @Override
  public final void removeDataChangedListener(@Nonnull final DataChangedListener<T> listener)
  {
    listeners.remove(listener);
  }

  @Override
  public final void removeDataChangedListeners()
  {
    listeners.clear();
  }

  /**
   * Notify listeners that our data has changed
   */
  private void notifyListeners()
  {
    // Always update
    for (final DataChangedListener<T> listener : listeners)
    {
      notifyListener(listener);
    }
  }

  /**
   * Notify listeners that our data has changed. Note that this always runs on the UI thread as it can update views
   */
  private void notifyListener(final DataChangedListener<T> listener)
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

  /**
   * @return {@code true} if the two data items are different, otherwise {@code false}
   */
  protected static <T> boolean dataDifferent(@Nullable final T oldData, @Nullable final T newData)
  {
    final boolean result;
    if (newData == null && oldData == null)
    {
      result = false;
    }
    else if (newData != null && oldData != null)
    {
      result = !oldData.equals(newData);
    }
    else
    {
      result = true;
    }
    return result;
  }
}