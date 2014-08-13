package com.wealdtech.android.fabric.condition;

import com.wealdtech.android.fabric.definition.ViewDefinition;
import com.wealdtech.android.fabric.action.ViewAction;

/**
 */
public abstract class ViewCondition
{
  public ViewDefinition definition;
  public ViewAction action;

  public ViewCondition()
  {
  }

  public void then(final ViewAction action)
  {
    this.action = action;
    setUp();
  }

  abstract void setUp();
}
