package com.wealdtech.android.fabric.action;

import android.view.View;
import com.wealdtech.android.fabric.condition.FocusChangeHandler;
import com.wealdtech.android.fabric.validator.Validator;

/**
 */
public class ValidateViewAction implements ViewAction, FocusChangeHandler
{
  public final Validator validator;
  public final ViewAction onValid;
  public final ViewAction onInvalid;

  public ValidateViewAction(final Validator validator,
                            final ViewAction onValid,
                            final ViewAction onInvalid)
  {
    this.validator = validator;
    this.onValid = onValid;
    this.onInvalid = onInvalid;
  }

  public static ValidateViewAction validate(final Validator validator, final ViewAction onValid, final ViewAction onInvalid)
  {
    return new ValidateViewAction(validator, onValid, onInvalid);
  }

  @Override
  public View.OnFocusChangeListener getFocusChangeListener(final View view)
  {
    return new View.OnFocusChangeListener()
    {
      @Override
      public void onFocusChange(final View v, final boolean hasFocus)
      {
        if (!hasFocus)
        {
          act(v);
        }
      }
    };
  }

  @Override
  public void act(final View view)
  {
    if (validator.validate(view))
    {
      onValid.act(view);
    }
    else
    {
      onInvalid.act(view);
    }
  }
}
