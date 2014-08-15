package com.wealdtech.android.fabric.condition;

/**
 * A condition which always returns {@code true}
 */
public class TrueCondition extends Condition
{
  public boolean isMet() { return true; }
}
