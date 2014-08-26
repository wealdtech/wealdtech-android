/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.test.providers;

import com.wealdtech.android.providers.AbstractProvider;
import com.wealdtech.android.providers.ConfigurationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Provide the date, updating every second
 */
public class DateProvider extends AbstractProvider<Date>
{
  private static final Logger LOG = LoggerFactory.getLogger(DateProvider.class);

  private static final Long DEFAULT_UPDATE_INTERVAL = 1000L;
  public DateProvider()
  {
    this(DEFAULT_UPDATE_INTERVAL);
  }

  public DateProvider(final long updateInterval)
  {
    super("Date", updateInterval);
    setConfigurationState(ConfigurationState.CONFIGURED);
  }

  public Date obtainData()
  {
    return new Date();
  }

  @Override
  public String getConfiguration()
  {
    // We have no configuration
    return null;
  }

  @Override
  public boolean canProvideData()
  {
    return true;
  }
}
