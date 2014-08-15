package com.wealdtech.android.fabric.action;

import android.view.View;
import android.widget.TextView;
import com.wealdtech.android.fabric.Rule;

import static com.wealdtech.Preconditions.checkState;

/**
 * An action to clear text on a {@link TextView} descendant
 */
public class ClearTextViewAction extends ViewAction
{
  private ClearTextViewAction(final View view)
  {
    super(view);
    checkState(view instanceof TextView, "Cannot run clearText() action against a non-textview");
  }

  public static ClearTextViewAction clearText(final View view)
  {
    return new ClearTextViewAction(view);
  }

  @Override
  public void act(final Rule dta)
  {
    ((TextView)view).setText(null);
  }
}
