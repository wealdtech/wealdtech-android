package com.wealdtech.android.fabric.trigger;

import com.wealdtech.android.fabric.Rule;

/**
 */
public abstract class Trigger
{
  /**
   * Set up the trigger
   */
  public abstract void setUp(final Rule dta);

  public static Trigger happens(Trigger trigger)
  {
    return trigger;
  }
}
