/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
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
public class EmptyValidator extends TextValidator
{
  private static EmptyValidator instance;

  private EmptyValidator()
  {
    super();
  }

  @Override
  public boolean validate(final View view)
  {
    final String val = ((TextView)view).getText().toString().trim();
    return val.length() == 0;
  }

  /**
   * A validator which validates that the text is empty
   */
  public static EmptyValidator emptyValidator()
  {
    if (instance == null)
    {
      synchronized (EmptyValidator.class)
      {
        // Double check
        if (instance == null)
        {
          instance = new EmptyValidator();
        }
      }
    }
    return instance;
  }
}
