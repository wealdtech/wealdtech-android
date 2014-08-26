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
import android.widget.Toast;
import com.wealdtech.android.fabric.Rule;

/**
 * An action to show an Android alert
 */
public class AlertViewAction extends ViewAction
{
  private String text;

  public AlertViewAction(final View view, final String text)
  {
    super(view);
    this.text = text;
  }

  public static AlertViewAction alert(final View view, final String alert)
  {
    return new AlertViewAction(view, alert);
  }

  @Override
  public void act(final Rule rule)
  {
    Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
  }
}
