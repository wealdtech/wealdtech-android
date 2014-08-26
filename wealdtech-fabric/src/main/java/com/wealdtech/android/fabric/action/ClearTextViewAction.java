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

import android.view.View;
import android.widget.TextView;
import com.wealdtech.android.fabric.Rule;

import static com.wealdtech.Preconditions.checkState;

/**
 * An action to clear text on a {@link TextView} descendant
 */
public class ClearTextViewAction extends ViewAction
{
  private ClearTextViewAction(final View view)
  {
    super(view);
    checkState(view instanceof TextView, "Cannot run clearText() action against a non-textview");
  }

  public static ClearTextViewAction clearText(final View view)
  {
    return new ClearTextViewAction(view);
  }

  @Override
  public void act(final Rule rule)
  {
    ((TextView)view).setText(null);
  }
}
