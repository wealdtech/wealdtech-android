package com.wealdtech.android.fabric.action;

import android.util.Log;
import android.view.View;
import com.wealdtech.android.fabric.condition.FocusChangeHandler;
import com.wealdtech.android.fabric.validator.Validator;

/**
 */
public class ValidateViewAction extends ViewAction implements FocusChangeHandler
{
  public final Validator validator;
  public final ViewAction onValid;
  public final ViewAction onInvalid;

  // Chained onFocusChangeListener
  private View.OnFocusChangeListener onFocusChangeListener;

  public ValidateViewAction(final Validator validator,
                            final ViewAction onValid,
                            final ViewAction onInvalid)
  {
    super();
    this.validator = validator;
    this.onValid = onValid;
    this.onInvalid = onInvalid;
    this.onFocusChangeListener = null;
  }

  public static ValidateViewAction validate(final Validator validator, final ViewAction onValid, final ViewAction onInvalid)
  {
    return new ValidateViewAction(validator, onValid, onInvalid);
  }

  @Override
  public View.OnFocusChangeListener getFocusChangeListener()
  {
    return new View.OnFocusChangeListener()
    {
      @Override
      public void onFocusChange(final View v, final boolean hasFocus)
      {
        Log.e("######################", "onFocusChange");
        if (!hasFocus)
        {
          act();
        }
        Log.e("######################", "onFocusChangeListener is " + onFocusChangeListener);
        if (onFocusChangeListener != null)
        {
          onFocusChangeListener.onFocusChange(definition.view, hasFocus);
        }
      }
    };
  }

  @Override
  public void setChainedFocusChangeListener(final View.OnFocusChangeListener listener)
  {
    this.onFocusChangeListener = listener;
  }

  @Override
  public void act()
  {
    if (validator.validate(definition.view))
    {
      onValid.act();
    }
    else
    {
      onInvalid.act();
    }
  }
}
