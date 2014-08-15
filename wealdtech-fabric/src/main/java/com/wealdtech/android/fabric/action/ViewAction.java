package com.wealdtech.android.fabric.action;

import android.view.View;

/**
 *
 */
public abstract class ViewAction extends Action
{
  public final View view;

  public ViewAction(final View view)
  {
    super();
    this.view = view;
  }
}
