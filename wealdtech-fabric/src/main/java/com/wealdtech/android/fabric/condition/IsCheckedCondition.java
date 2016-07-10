package com.wealdtech.android.fabric.condition;

import android.widget.CompoundButton;

/**
 * A condition which returns {@code true} if the view is checked
 */
public class IsCheckedCondition extends ViewCondition
{
  public IsCheckedCondition(CompoundButton view)
  {
    super(view);
  }

  public static IsCheckedCondition isChecked(CompoundButton view)
  {
    return new IsCheckedCondition(view);
  }

  @Override
  public boolean isMet()
  {
    return ((CompoundButton)view).isChecked();
  }
}