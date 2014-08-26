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
 * An action to unpersist data in Fabric
 */
public class UnpersistDataAction extends FabricAction
{
  private final Activity activity;
  private final String component;
  private final String key;

  private UnpersistDataAction(final Activity activity, final String component, final String key)
  {
    super();
    this.activity = activity;
    this.component = component;
    this.key = key;

    checkState(activity != null && key != null, "unpersist() requires an activity and a key");
  }

  public static UnpersistDataAction unpersist(final Activity activity, final String key) { return new UnpersistDataAction(activity, null, key); }
  public static UnpersistDataAction unpersist(final Activity activity, final String component, final String key) { return new UnpersistDataAction(activity, component, key); }
  public static UnpersistDataAction unpersist(final Activity activity, final Integer component, final String key) { return new UnpersistDataAction(activity, Integer.toString(component), key); }

  @Override
  public void act(final Rule rule)
  {
    if (component == null)
    {
      Fabric.getInstance().unpersist(activity, key);
    }
    else
    {
      Fabric.getInstance().unpersist(activity, component, key);
    }
  }
}
