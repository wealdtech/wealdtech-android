package com.wealdtech.android.fabric.action;

import android.view.View;
import com.wealdtech.android.fabric.definition.ViewDefinition;

/**
 *
 */
public abstract class ViewAction extends Action
{
  ViewDefinition definition;

  View.OnFocusChangeListener onFocusChangeListener;

  public ViewAction()
  {
  }

  abstract void act();

  public void setChainedFocusChangeListener(final View.OnFocusChangeListener listener)
  {
    this.onFocusChangeListener = listener;
  }
}
