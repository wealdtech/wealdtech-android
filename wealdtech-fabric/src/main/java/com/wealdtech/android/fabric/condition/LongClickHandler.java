package com.wealdtech.android.fabric.condition;

import android.view.View;
import android.widget.Button;

/**
 */
public interface LongClickHandler
{
  Button.OnLongClickListener getLongClickListener(View view);
}
