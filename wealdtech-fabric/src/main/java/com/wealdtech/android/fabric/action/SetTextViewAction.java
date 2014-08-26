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
import com.wealdtech.android.fabric.Generator;
import com.wealdtech.android.fabric.Rule;

import static com.wealdtech.Preconditions.checkState;

/**
 * SetTextViewAction alters the text for a TextView
 */
public class SetTextViewAction<T> extends ViewAction
{
  private String text;
  private Generator<String> generator;

  private SetTextViewAction(final View view, final String text, final Generator<String> generator)
  {
    super(view);
    checkState(view instanceof TextView, "Cannot run setText() action against a non-textview");
    this.text = text;
    this.generator = generator;
    checkState(text != null || generator != null, "setText() requires either a text string or a generator");
  }

  public static SetTextViewAction setText(final View view, final String alert)
  {
    return new SetTextViewAction(view, alert, null);
  }
  public static SetTextViewAction setText(final View view, final Generator<String> generator)
  {
    return new SetTextViewAction(view, null, generator);
  }

  @Override
  public void act(final Rule rule)
 {
   final String text;
   if (generator != null)
   {
     text = generator.generate();
   }
   else
   {
     text = this.text;
   }
   ((TextView)view).setText(text);
  }
}
