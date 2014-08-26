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
import com.wealdtech.android.fabric.Fabric;
import com.wealdtech.android.fabric.Rule;

import static com.wealdtech.Preconditions.checkState;

/**
 * An action to persist data in Fabric
 */
public class PersistDataAction extends FabricAction
{
  private final Activity activity;
  private final String component;
  private final String key;

  private PersistDataAction(final Activity activity, final String component, final String key)
  {
    super();
    this.activity = activity;
    this.component = component;
    this.key = key;

    checkState(activity != null && key != null, "persist() requires an activity and a key");
  }

  public static PersistDataAction persist(final Activity activity, final String key) { return new PersistDataAction(activity, null, key); }
  public static PersistDataAction persist(final Activity activity, final String component, final String key) { return new PersistDataAction(activity, component, key); }
  public static PersistDataAction persist(final Activity activity, final Integer component, final String key) { return new PersistDataAction(activity, Integer.toString(component), key); }

  @Override
  public void act(final Rule rule)
  {
    if (component == null)
    {
      Fabric.getInstance().persist(activity, key);
    }
    else
    {
      Fabric.getInstance().persist(activity, component, key);
    }
  }
}
