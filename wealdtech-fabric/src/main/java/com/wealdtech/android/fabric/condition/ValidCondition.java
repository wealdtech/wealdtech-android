/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric.condition;

import android.view.View;
import com.wealdtech.android.fabric.validator.Validator;

/**
 */
public class ValidCondition extends ViewCondition
{
  private final Validator validator;

  private ValidCondition(final View view, final Validator validator)
  {
    super(view);
    this.validator = validator;
  }

  @Override
  public boolean isMet()
  {
    return validator.validate(view);
  }

  /**
   * Check if a view is valid according to its validator
   */
  public static ValidCondition valid(final View view, final Validator validator)
  {
    return new ValidCondition(view, validator);
  }

  /**
   * Run a validator immediately
   */
  public static boolean isValid(final View view, final Validator validator)
  {
    return new ValidCondition(view, validator).isMet();
  }
}
