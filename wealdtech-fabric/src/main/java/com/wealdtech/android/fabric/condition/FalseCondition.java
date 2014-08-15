package com.wealdtech.android.fabric.condition;

/**
 * A condition which always returns {@code false}
 */
public class FalseCondition extends Condition
{
  public boolean isMet() { return false; }
}
