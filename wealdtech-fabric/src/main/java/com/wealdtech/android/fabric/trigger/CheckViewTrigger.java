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

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.wealdtech.android.fabric.Rule;

/**
 */
public class CheckViewTrigger extends CompoundButtonTrigger
{
  public CheckViewTrigger(final CompoundButton view)
  {
    super(view);
  }

  @Override
  public void setUp(final Rule dta)
  {
    view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(final CompoundButton compoundButton, final boolean b)
      {
        dta.act();
      }
    });
 }

  void tearDown()
  {

  }

  public static CheckViewTrigger check(final CompoundButton view)
  {
    return new CheckViewTrigger(view);
  }
}
