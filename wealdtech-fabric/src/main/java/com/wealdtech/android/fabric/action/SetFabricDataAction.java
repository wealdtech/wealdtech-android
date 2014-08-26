/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric.action;

import android.app.Activity;
import android.util.Log;
import com.wealdtech.android.fabric.Fabric;
import com.wealdtech.android.fabric.Generator;
import com.wealdtech.android.fabric.Rule;

import static com.wealdtech.Preconditions.checkState;

/**
 * An action to set data in Fabric
 */
public class SetFabricDataAction<T> extends FabricAction
{
  private final Activity activity;
  private final String component;
  private final String key;
  private final Generator<T> generator;

  private SetFabricDataAction(final Activity activity, final String component, final String key, final Generator<T> generator)
  {
    super();
    this.activity = activity;
    this.component = component;
    this.key = key;
    this.generator = generator;

    checkState(key != null && generator != null, "persist() requires a key and a generator");
  }

  public static <T> SetFabricDataAction<T> setFabricData(final Activity activity, final String key, Generator<T> generator) { return new SetFabricDataAction<>(activity, null, key, generator); }
  public static <T> SetFabricDataAction<T> setFabricData(final Activity activity, final String component, final String key, final Generator<T> generator) { return new SetFabricDataAction<>(activity, component, key, generator); }
  public static <T> SetFabricDataAction<T> setFabricData(final Activity activity, final Integer component, final String key, final Generator<T> generator) { return new SetFabricDataAction<>(activity, Integer.toString(component), key, generator); }

  @Override
  public void act(final Rule rule)
  {
    if (component == null)
    {
      if (activity == null)
      {
        Fabric.getInstance().set(key, generator.generate());
      }
      else
      {
        Fabric.getInstance().set(activity, key, generator.generate());
      }
    }
    else
    {
      Fabric.getInstance().set(activity, component, key, generator.generate());
    }
  }
}
