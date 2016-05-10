/*
 * Copyright 2012 - 2016 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.dataproviders;

import android.os.AsyncTask;
import com.wealdtech.roberto.DataProviderConfigurationState;
import com.wealdtech.roberto.DataProviderFailure;
import com.wealdtech.roberto.DataProviderState;
import com.wealdtech.roberto.dataprovider.AbstractDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import javax.annotation.Nullable;

/**
 * An extension to the abstract data provider which obtains data from the network using an AsyncTask to separate out fetching and
 * setting the data
 */
public abstract class AbstractNetworkDataProvider<T> extends AbstractDataProvider<T>
{
  private static final Logger LOG = LoggerFactory.getLogger(AbstractNetworkDataProvider.class);

  public AbstractNetworkDataProvider(final String name)
  {
    super(name);
  }

  /**
   * Fetch the data asynchronously
   */
  @Override
  public void fetch()
  {
    new AsyncTask<Void, Void, T>()
    {
      @Override
      @Nullable
      protected T doInBackground(final Void... params)
      {
        if (getConfigurationState() == DataProviderConfigurationState.NOT_CONFIGURED)
        {
          throw new IllegalStateException(getName() + " provider not configured to provide data");
        }
        if (getProviderState() == DataProviderState.NOT_PROVIDING || getProviderState() == DataProviderState.AWAITING_CONFIGURATION)
        {
          throw new IllegalStateException(getName() + " provider not providing data");
        }

        try
        {
          return obtainData();
        }
        catch (final Exception e)
        {
          LOG.warn("Failed to obtain data from network: {}", e);
          if (e instanceof RetrofitError)
          {
            final RetrofitError re = (RetrofitError)e;
            LOG.error("Error kind is {}", re.getKind());
            switch(re.getKind())
            {
              case NETWORK:
                notifyFailure(DataProviderFailure.TRANSPORT);
                break;
              case HTTP:
                notifyFailure(DataProviderFailure.SERVICE);
                break;
              default:
                notifyFailure(DataProviderFailure.FAILED);
                break;
            }
          }
          else
          {
            LOG.error("Exception is {}", e.getClass());
            notifyFailure(DataProviderFailure.FAILED);
          }
          return null;
        }
      }

      @Override
      protected void onPostExecute (@Nullable T data)
      {
        setData(data);
      }
    }.execute();
  }
}
