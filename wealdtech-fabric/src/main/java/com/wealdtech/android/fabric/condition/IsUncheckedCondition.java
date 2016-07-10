package com.wealdtech.android.fabric.condition;

import android.widget.CompoundButton;

/**
 * A condition which returns {@code true} if the view is unchecked
 */
public class IsUncheckedCondition extends ViewCondition
{
  public IsUncheckedCondition(CompoundButton view)
  {
    super(view);
  }

  public static IsUncheckedCondition isUnchecked(CompoundButton view)
  {
    return new IsUncheckedCondition(view);
  }

  @Override
  public boolean isMet()
  {
    return !((CompoundButton)view).isChecked();
  }
}