package com.wealdtech.android.fabric.condition;

import android.view.View;

/**
 */
public interface FocusChangeHandler
{
  View.OnFocusChangeListener getFocusChangeListener();
  void setChainedFocusChangeListener(View.OnFocusChangeListener listener);
}
