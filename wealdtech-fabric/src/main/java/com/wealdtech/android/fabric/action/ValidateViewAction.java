package com.wealdtech.android.fabric.action;

import android.view.View;
import com.wealdtech.android.fabric.Rule;
import com.wealdtech.android.fabric.validator.Validator;

/**
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
  public void act(final Rule dta)
  {
    if (validator.validate(view))
    {
      onValid.act(dta);
    }
    else
    {
      onInvalid.act(dta);
    }
  }
}
