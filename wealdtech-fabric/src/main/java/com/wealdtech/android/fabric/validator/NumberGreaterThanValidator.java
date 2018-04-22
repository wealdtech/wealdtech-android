/*
 * Copyright 2012 - 2018 Weald Technology Trading Limited
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

import java.math.BigDecimal;

/**
 */
public class NumberGreaterThanValidator extends TextValidator
{
  private BigDecimal min;

  private NumberGreaterThanValidator(final BigDecimal min)
  {
    super();
    this.min = min;
  }

  @Override
  public boolean validate(final View view)
  {
    final String val = ((TextView)view).getText().toString().trim();
    try
    {
      final BigDecimal bd = new BigDecimal(val);
      return bd.compareTo(min) > 0;
    }
    catch (final Exception ignored)
    {
      return false;
    }
  }

  /**
   * A validator which validates that the text is a valid number larger than the provided value
   */
  public static NumberGreaterThanValidator numberGreaterThanValidator(final BigDecimal min)
  {
    return new NumberGreaterThanValidator(min);
  }
}
