package com.wealdtech.android.fabric.action;

import android.view.View;
import com.wealdtech.android.fabric.Rule;

/**
 * An action to set the background color on a {@link android.widget.TextView} descendant
 */
public class SetBackgroundColorViewAction extends ViewAction
{
  private final int color;

  private SetBackgroundColorViewAction(final View view, final int color)
  {
    super(view);
    this.color = color;
  }

  public static SetBackgroundColorViewAction setBackgroundColor(final View view, final int color)
  {
    return new SetBackgroundColorViewAction(view, color);
  }

  @Override
  public void act(final Rule dta)
  {
    view.setBackgroundColor(color);
  }
}
