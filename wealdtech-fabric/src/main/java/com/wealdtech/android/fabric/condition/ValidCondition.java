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
