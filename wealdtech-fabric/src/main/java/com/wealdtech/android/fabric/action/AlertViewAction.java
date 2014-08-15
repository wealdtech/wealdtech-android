package com.wealdtech.android.fabric.action;

import android.view.View;
import android.widget.Toast;
import com.wealdtech.android.fabric.Rule;

/**
 */
public class AlertViewAction extends ViewAction
{
  private String text;

  public AlertViewAction(final View view, final String text)
  {
    super(view);
    this.text = text;
  }

  public static AlertViewAction alert(final View view, final String alert)
  {
    return new AlertViewAction(view, alert);
  }

  @Override
  public void act(final Rule dta)
  {
    Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
  }
}
