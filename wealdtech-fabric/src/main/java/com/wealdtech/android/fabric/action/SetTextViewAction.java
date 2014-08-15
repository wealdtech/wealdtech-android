package com.wealdtech.android.fabric.action;

import android.view.View;
import android.widget.TextView;
import com.wealdtech.android.fabric.Rule;

import static com.wealdtech.Preconditions.checkState;

/**
 */
public class SetTextViewAction<T> extends ViewAction
{
  private String text;

  private SetTextViewAction(final View view, final String text)
  {
    super(view);
    checkState(view instanceof TextView, "Cannot run setText() action against a non-textview");
    this.text = text;
  }

  public static SetTextViewAction setText(final View view, final String alert)
  {
    return new SetTextViewAction(view, alert);
  }

  @Override
  public void act(final Rule dta)
  {
    ((TextView)view).setText(text);
  }
}
