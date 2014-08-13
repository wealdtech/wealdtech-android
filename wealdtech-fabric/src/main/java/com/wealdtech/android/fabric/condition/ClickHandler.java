package com.wealdtech.android.fabric.condition;

import android.view.View;
import android.widget.Button;

/**
 */
public interface ClickHandler
{
  Button.OnClickListener getClickListener(View view);
}
