/*
 * Copyright 2012 - 2015 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric.trigger;

import android.view.View;
import android.widget.AdapterView;
import com.wealdtech.android.fabric.Rule;

/**
 */
public class ItemSelectViewTrigger extends AdapterViewTrigger
{
  public ItemSelectViewTrigger(final AdapterView view)
  {
    super(view);
  }

  private AdapterView.OnItemClickListener listener = null;

  @Override
  public void setUp(final Rule dta)
  {
    listener = new AdapterView.OnItemClickListener()
    {
      @Override
      public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l)
      {
        dta.act();
      }
    };

    view.setOnItemClickListener(listener);
  }

  void tearDown()
  {
    if (listener != null)
    {
      view.setOnItemClickListener(null);
    }
  }

  public static ItemSelectViewTrigger itemSelect(final AdapterView view)
  {
    return new ItemSelectViewTrigger(view);
  }
}
