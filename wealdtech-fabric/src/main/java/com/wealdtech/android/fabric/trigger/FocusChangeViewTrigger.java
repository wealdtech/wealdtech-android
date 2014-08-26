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
import com.wealdtech.android.fabric.Rule;

/**
 */
public class FocusChangeViewTrigger extends ViewTrigger
{
  private final boolean onGain;
  private final boolean onLoss;

  public FocusChangeViewTrigger(final View view,
                                final boolean onGain,
                                final boolean onLoss)
  {
    super(view);
    this.onGain = onGain;
    this.onLoss = onLoss;
  }

  @Override
  public void setUp(final Rule dta)
  {
    // Need to handle chaining
    final View.OnFocusChangeListener chainedListener = view.getOnFocusChangeListener();

    view.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      @Override
      public void onFocusChange(final View v, final boolean hasFocus)
      {
        if (onGain && hasFocus)
        {
          dta.act();
        }
        else if (onLoss && !hasFocus)
        {
          dta.act();
        }
        if (chainedListener != null)
        {
          chainedListener.onFocusChange(v, hasFocus);
        }
      }
    });
 }

  void tearDown()
  {

  }

  public static FocusChangeViewTrigger focusLost(final View view)
  {
    return new FocusChangeViewTrigger(view, false, true);
  }

  public static FocusChangeViewTrigger focusGain(final View view)
  {
    return new FocusChangeViewTrigger(view, true, false);
  }

  public static FocusChangeViewTrigger focusChange(final View view)
  {
    return new FocusChangeViewTrigger(view, true, true);
  }
}
