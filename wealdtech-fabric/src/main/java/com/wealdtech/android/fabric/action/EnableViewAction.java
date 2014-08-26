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
import com.wealdtech.android.fabric.Rule;

/**
 * An action to enable an Android view
 */
public class EnableViewAction extends ViewAction
{
  private EnableViewAction(final View view)
  {
    super(view);
  }

  public static EnableViewAction enable(final View view)
  {
    return new EnableViewAction(view);
  }

  @Override
  public void act(final Rule rule)
  {
    view.setEnabled(true);
  }
}
