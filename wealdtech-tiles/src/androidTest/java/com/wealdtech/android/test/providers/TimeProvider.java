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

import com.wealdtech.android.providers.ChainedProvider;
import com.wealdtech.android.providers.ConfigurationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Example of a chained provider, obtaining data from the DateProvider and providing a formatted version as a string
 */
public class TimeProvider extends ChainedProvider<String, Date>
{
  private static final Logger LOG = LoggerFactory.getLogger(TimeProvider.class);
  private String text;

  public TimeProvider()
  {
    super("Time", 0);
    setConfigurationState(ConfigurationState.CONFIGURED);
  }

  @Override
  public String obtainData()
  {
    return text;
  }

  @Override
  public String getConfiguration()
  {
    return text;
  }

  @Override
  public boolean canProvideData()
  {
    return true;
  }

  @Override
  public void onDataChanged(@Nullable final Date data)
  {
    text = data == null ? null : "Time: " + DateFormat.getTimeInstance().format(data);
  }
}
