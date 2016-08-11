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

import java.net.MalformedURLException;
import java.net.URL;

/**
 */
public class UrlValidator extends TextValidator
{
  private static UrlValidator instance;

  private UrlValidator()
  {
    super();
  }

  @Override
  public boolean validate(final View view)
  {
    final String val = ((TextView)view).getText().toString().trim();
    try
    {
      new URL(val);
      return true;
    }
    catch (final MalformedURLException ignored)
    {
      return false;
    }
  }

  /**
   * A validator which validates that the text is a valid URL
   */
  public static UrlValidator urlValidator()
  {
    if (instance == null)
    {
      synchronized (UrlValidator.class)
      {
        // Double check
        if (instance == null)
        {
          instance = new UrlValidator();
        }
      }
    }
    return instance;
  }
}