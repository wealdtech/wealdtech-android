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

import android.widget.TextView;
import com.wealdtech.android.fabric.Rule;

/**
 * An action to set the text color of an Android textview
 */
public class TextColorAction extends ViewAction
{
  private final int color;

  private TextColorAction(final TextView view, final int color)
  {
    super(view);
    this.color = color;
  }

  public static TextColorAction textColor(final TextView view, final int color)
  {
    return new TextColorAction(view, color);
  }

  @Override
  public void act(final Rule rule)
  {
    ((TextView)view).setTextColor(color);
  }
}
