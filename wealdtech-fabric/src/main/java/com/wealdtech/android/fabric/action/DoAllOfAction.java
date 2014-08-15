package com.wealdtech.android.fabric.action;

import com.google.common.collect.ImmutableSet;
import com.wealdtech.android.fabric.Rule;

/**
 */
public class DoAllOfAction extends Action
{
  private final ImmutableSet<Action> actions;

  private DoAllOfAction(final Action... actions)
  {
    this.actions = ImmutableSet.copyOf(actions);
  }

  public void act(final Rule dta)
  {
    for (final Action action : actions)
    {
      action.act(dta);
    }
  }

  public static Action doAllOf(final Action... actions)
  {
    return new DoAllOfAction(actions);
  }
}
