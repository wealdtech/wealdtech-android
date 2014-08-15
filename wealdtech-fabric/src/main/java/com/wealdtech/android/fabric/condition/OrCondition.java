package com.wealdtech.android.fabric.condition;

import com.google.common.collect.ImmutableSet;

/**
 */
public class OrCondition extends Condition
{
  private final ImmutableSet<Condition> conditions;

  private OrCondition(final Condition... conditions)
  {
    this.conditions = ImmutableSet.copyOf(conditions);
  }

  @Override
  public boolean isMet()
  {
    for (final Condition condition : conditions)
    {
      if (condition.isMet())
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Compound condition which is met if any included conditions are met
   */
  public static Condition anyOf(Condition... conditions)
  {
    return new OrCondition(conditions);
  }
}
