package com.wealdtech.android.fabric.condition;

import com.wealdtech.android.fabric.action.ViewAction;
import com.wealdtech.android.fabric.definition.FabricDataDefinition;

/**
 */
public abstract class FabricDataCondition
{
  public FabricDataDefinition definition;
  public ViewAction action;

  public FabricDataCondition()
  {
  }

  public void then(final ViewAction action)
  {
    this.action = action;
    setUp();
  }

  abstract void setUp();
}
