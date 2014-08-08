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

  private final List<DataChangedListener<T>> dataListeners = new ArrayList<>();
  private final List<ConfigurationChangedListener> configurationListeners = new ArrayList<>();

  private T data;

  private ProviderState providerState;
  private ConfigurationState configurationState;

  private boolean obtainedData;

  private final String name;

  private final long updateInterval;

  private AsyncTask<Void, Void, Void> pollingTask;

  public AbstractProvider(final long updateInterval)
  {
    this("Provider", updateInterval);
  }

  public AbstractProvider(final String name, final long updateInterval)
  {
    this.name = name;
    this.updateInterval = updateInterval;
    this.providerState = ProviderState.NOT_PROVIDING;
    this.configurationState = ConfigurationState.NOT_CONFIGURED;
  }

  public void startProviding() throws IllegalStateException
  {
    if (providerState == ProviderState.PROVIDING)
    {
      throw new IllegalStateException("Already providing data");
    }
    if (configurationState == ConfigurationState.NOT_CONFIGURED)
    {
      // We want to provide but aren't configured; note it but nothing else
      providerState = ProviderState.AWAITING_CONFIGURATION;
    }
    else
    {
      if (isPollingProvider())
      {
        startPolling();
      }
      else
      {
        fetchOnce();
      }
      providerState = ProviderState.PROVIDING;
    }
  }

  private void startPolling()
  {
    if (pollingTask != null)
    {
      pollingTask.cancel(true);
      pollingTask = null;
    }

    // We have yet to obtain data since we started polling...
    obtainedData = false;

    pollingTask = new AsyncTask<Void, Void, Void>()
    {
      @Override
      protected Void doInBackground(final Void... params)
      {
        Thread.currentThread().setName(name + " provider");
        while (!isCancelled())
        {
          if (canProvideData())
          {
            final T newData = obtainData();
            final T oldData = data;
            setData(newData);
            obtainedData = true;
            if (dataDifferent(oldData, newData))
            {
              publishProgress();
            }
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
        notifyDataListeners();
      }
    };
    // Run in thread pool for real multi-threading
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
    {
      pollingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
    }
    else
    {
      pollingTask.execute(null, null, null);
    }
  }

  protected void fetchOnce()
  {
    obtainedData = false;
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
        setData(result);
        obtainedData = true;
        if (dataDifferent(oldData, result))
        {
          notifyDataListeners();
        }
      }
    };
    // Run in thread pool for real multi-threading
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
    {
      task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
    }
    else
    {
      task.execute(null, null, null);
    }
  }

  protected void setData(final T data)
  {
    this.data = data;
  }

  public void stopProviding()
  {
    providerState = ProviderState.NOT_PROVIDING;
    if (pollingTask != null)
    {
      pollingTask.cancel(true);
      pollingTask = null;
    }
  }

  public abstract T obtainData();

  public final T getData() throws IllegalStateException
  {
    if (configurationState != ConfigurationState.CONFIGURED)
    {
      throw new IllegalStateException("Attempt to obtain data from provider which is not configured");
    }
    if (providerState != ProviderState.PROVIDING)
    {
      throw new IllegalStateException("Attempt to obtain data from provider which is not providing");
    }
    return data;
  }

  public void setConfigurationState(final ConfigurationState configurationState) throws IllegalStateException
  {
    if (configurationState == ConfigurationState.CONFIGURED && !canProvideData())
    {
      throw new IllegalStateException("Attempt to set configured when not able to provide data");
    }
    if (configurationState != this.configurationState)
    {
      this.configurationState = configurationState;
      // If we go from configured to not configured we might need to stop polling
      if (configurationState == ConfigurationState.NOT_CONFIGURED && this.providerState == ProviderState.PROVIDING)
      {
        stopProviding();
      }
      else if (configurationState == ConfigurationState.CONFIGURED)
      {
        if (this.providerState == ProviderState.PROVIDING)
        {
          // Currently providing so need to restart
          stopProviding();
          startProviding();
        }
        else if (this.providerState == ProviderState.AWAITING_CONFIGURATION)
        {
          // We couldn't provide because we didn't have configuration but now we do.
          startProviding();
        }
      }
      notifyConfigurationListeners();
    }
  }

  /**
   * {@inheritDoc} If this provider is currently providing then it will update the passed-in listener immediately
   */
  @Override
  public final void addDataChangedListener(@Nonnull final DataChangedListener<T> listener)
  {
    dataListeners.add(listener);
    if (providerState == ProviderState.PROVIDING && obtainedData)
    {
      notifyDataListener(listener);
    }
  }

  @Override
  public final void removeDataChangedListener(@Nonnull final DataChangedListener<T> listener)
  {
    dataListeners.remove(listener);
  }

  @Override
  public final void removeDataChangedListeners()
  {
    dataListeners.clear();
  }


  @Override
  public final void addConfigurationChangedListener(@Nonnull final ConfigurationChangedListener listener)
  {
    configurationListeners.add(listener);
    notifyConfigurationListener(listener);
  }

  @Override
  public final void removeConfigurationChangedListener(@Nonnull final ConfigurationChangedListener listener)
  {
    configurationListeners.remove(listener);
  }

  @Override
  public final void removeConfigurationChangedListeners()
  {
    configurationListeners.clear();
  }

  /**
   * Notify listeners that our data has changed
   */
  protected void notifyDataListeners()
  {
    // Always update
    for (final DataChangedListener<T> listener : dataListeners)
    {
      notifyDataListener(listener);
    }
  }

  /**
   * Notify listeners that our data has changed. Note that this always runs on the UI thread as it can update views
   */
  private void notifyDataListener(final DataChangedListener<T> listener)
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
   * Notify listeners that our configuration has changed
   */
  protected void notifyConfigurationListeners()
  {
    for (final ConfigurationChangedListener listener : configurationListeners)
    {
      notifyConfigurationListener(listener);
    }
  }

  /**
   * Notify listeners that our configuration has changed.
   */
  private void notifyConfigurationListener(final ConfigurationChangedListener listener)
  {
    listener.onConfigurationChanged(configurationState);
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

  @Override
  public boolean canProvideData()
  {
    return configurationState == ConfigurationState.CONFIGURED && providerState == ProviderState.PROVIDING;
  }

  /**
   * Help to state if we are a polling provider.  Note that this gives no indication if the provider is currently polling.
   * @return {@code true} if we are a polling provider; otherwise {@code false}
   */
  private boolean isPollingProvider()
  {
    return updateInterval > 0;
  }

  public String getName()
  {
    return this.name;
  }

  public long getUpdateInterval()
  {
    return this.updateInterval;
  }
}
