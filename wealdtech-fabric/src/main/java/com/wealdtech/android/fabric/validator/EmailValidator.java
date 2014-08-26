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
public class EmailValidator extends TextValidator
{
  private static EmailValidator instance;

  private EmailValidator()
  {
    super();
  }

  @Override
  public boolean validate(final View view)
  {
    final String val = ((TextView)view).getText().toString().trim();
    return val.contains("@") && (val.indexOf("@") != 0) && (val.indexOf("@") != val.length() - 1);
  }

  /**
   * Validate that the view contains something which could be considered an email address.
   * Due to the complexity of email addresses all this does is check for the presence of @ with some text either side
   */
  public static EmailValidator emailValidator()
  {
    if (instance == null)
    {
      synchronized (EmailValidator.class)
      {
        // Double check
        if (instance == null)
        {
          instance = new EmailValidator();
        }
      }
    }
    return instance;
  }
}
