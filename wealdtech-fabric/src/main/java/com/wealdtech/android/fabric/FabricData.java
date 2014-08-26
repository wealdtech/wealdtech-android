/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric;

import android.app.Activity;
import com.google.common.base.Optional;

/**
 */
public class FabricData
{
  public final Optional<String> activity;
  public final Optional<String> component;
  public final String key;

  private FabricData(final Activity activity, final String component, final String key)
  {
    if (activity == null)
    {
      this.activity = Optional.absent();
    }
    else
    {
      this.activity = Optional.fromNullable(activity.getLocalClassName());
    }
    this.component = Optional.fromNullable(component);
    this.key = key;
  }

  public static FabricData fabricData(final Activity activity, final String key)
  {
    return new FabricData(activity, null, key);
  }

  public static FabricData fabricData(final Activity activity, final int component, final String key)
  {
    return new FabricData(activity, Integer.toString(component), key);
  }

  public static FabricData fabricData(final Activity activity, final String component, final String key)
  {
    return new FabricData(activity, component, key);
  }

  public static FabricData fabricData(final String key)
  {
    return new FabricData(null, null, key);
  }
}
