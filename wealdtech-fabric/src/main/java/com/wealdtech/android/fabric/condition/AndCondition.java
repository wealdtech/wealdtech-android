package com.wealdtech.android.fabric.condition;

import com.google.common.collect.ImmutableSet;

/**
 */
public class AndCondition extends Condition
{
  private final ImmutableSet<Condition> conditions;

  private AndCondition(final Condition... conditions)
  {
    this.conditions = ImmutableSet.copyOf(conditions);
  }

  @Override
  public boolean isMet()
  {
    for (final Condition condition : conditions)
    {
      if (!condition.isMet())
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Compound condition which is met if all included conditions are met
   */
  public static Condition allOf(Condition... conditions)
  {
    return new AndCondition(conditions);
  }
}
