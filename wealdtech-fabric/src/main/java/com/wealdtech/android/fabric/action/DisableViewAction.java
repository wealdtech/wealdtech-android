package com.wealdtech.android.fabric.action;

import android.view.View;
import com.wealdtech.android.fabric.Rule;

/**
 */
public class DisableViewAction extends ViewAction
{
  private DisableViewAction(final View view)
  {
    super(view);
  }

  public static DisableViewAction disable(final View view)
  {
    return new DisableViewAction(view);
  }

  @Override
  public void act(final Rule dta)
  {
    view.setEnabled(false);
  }
}
