/*
 * Copyright 2012 - 2015 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric.validator;

import android.view.View;
import android.widget.TextView;

/**
 */
public class LengthValidator extends TextValidator
{
  private int min;

  private LengthValidator(int min)
  {
    super();
    this.min = min;
  }

  @Override
  public boolean validate(final View view)
  {
    final String val = ((TextView)view).getText().toString().trim();
    return val.length() >= min;
  }

  /**
   * A validator which validates that the text present contains at least a given number of characters
   */
  public static LengthValidator lengthValidator(final int length)
  {
    return new LengthValidator(length);
  }
}
