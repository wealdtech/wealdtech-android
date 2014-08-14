package com.wealdtech.android.fabric.condition;

import com.wealdtech.android.fabric.action.Action;
import com.wealdtech.android.fabric.definition.Definition;

/**
 */
public abstract class Condition
{
  public Definition definition;
  public Action action;


  public void then(final Action action)
  {
    this.action = action;
    setUp();
  }

  abstract void setUp();
}
