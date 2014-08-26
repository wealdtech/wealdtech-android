/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric.trigger;

import com.wealdtech.android.fabric.Rule;
import com.wealdtech.android.fabric.Fabric;
import com.wealdtech.android.fabric.FabricData;
import com.wealdtech.android.fabric.FabricDataListener;

/**
 */
public class ChangeFabricTrigger extends FabricTrigger implements FabricDataListener
{
  // TODO this is not a good idea; how do we avoid this?
  private Rule dta;

  public ChangeFabricTrigger(final FabricData data)
  {
    super(data);
  }

  @Override
  public void setUp(final Rule dta)
  {
    this.dta = dta;
    if (data.component.isPresent())
    {
      Fabric.getInstance().addListener(data.activity.get(), data.component.orNull(), data.key, this);
    }
    else if (data.activity.isPresent())
    {
      Fabric.getInstance().addListener(data.activity.get(), data.key, this);
    }
    else
    {
      Fabric.getInstance().addListener(data.key, this);
    }
  }

  @Override
  public void onDataChanged(final Object oldData, final Object newData)
  {
    dta.act();
  }

  public static ChangeFabricTrigger change(final FabricData data)
  {
    return new ChangeFabricTrigger(data);
  }
}
