package com.wealdtech.android.fabric.trigger;

import android.view.View;

/**
 * A trigger related to an android View
 */
public abstract class ViewTrigger extends Trigger
{
  public final View view;

  public ViewTrigger(final View view)
  {
    super();
    this.view = view;
  }
}
