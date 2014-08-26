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
import com.wealdtech.android.fabric.validator.Validator;

/**
 * A ValidateViewAction uses a {@link Validator} to select which of two possible actions to run
 */
public class ValidateViewAction extends ViewAction
{
  public final Validator validator;
  public final Action onValid;
  public final Action onInvalid;

  public ValidateViewAction(final View view,
                            final Validator validator,
                            final Action onValid,
                            final Action onInvalid)
  {
    super(view);
    this.validator = validator;
    this.onValid = onValid;
    this.onInvalid = onInvalid;
  }

  /**
   * Carry out an action according to the results of a validator
   */
  public static ValidateViewAction validate(final View view, final Validator validator, final Action onValid, final Action onInvalid)
  {
    return new ValidateViewAction(view, validator, onValid, onInvalid);
  }

  @Override
  public void act(final Rule rule)
  {
    if (validator.validate(view))
    {
      onValid.act(rule);
    }
    else
    {
      onInvalid.act(rule);
    }
  }
}
