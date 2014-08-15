package com.wealdtech.android.fabric.action;

import android.view.View;
import com.wealdtech.android.fabric.Rule;

/**
 */
public class EnableViewAction extends ViewAction
{
  private EnableViewAction(final View view)
  {
    super(view);
  }

  public static EnableViewAction enable(final View view)
  {
    return new EnableViewAction(view);
  }

  @Override
  public void act(final Rule dta)
  {
    view.setEnabled(true);
  }
}
