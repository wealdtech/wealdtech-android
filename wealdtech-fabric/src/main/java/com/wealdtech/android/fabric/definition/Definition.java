package com.wealdtech.android.fabric.definition;

import com.wealdtech.android.fabric.condition.Condition;

/**
 */
public class Definition
{
  public Condition encounters(final Condition condition)
  {
    condition.definition = this;
    return condition;
  }
}
