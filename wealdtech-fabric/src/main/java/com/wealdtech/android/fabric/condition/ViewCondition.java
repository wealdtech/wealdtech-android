package com.wealdtech.android.fabric.condition;

import android.view.View;

/**
 */
public abstract class ViewCondition extends Condition
{
  public final View view;

  public ViewCondition(final View view)
  {
    this.view = view;
  }
}
