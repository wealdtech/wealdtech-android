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

/**
 * Provide preset text
 */
public class PresetTextProvider extends AbstractProvider<String>
{
  private final String text;

  public PresetTextProvider(final String text)
  {
    super("Preset text", 0);
    this.text = text;
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
}
