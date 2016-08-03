package com.wealdtech.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * TODO: document your custom view class.
 */
public class PayView extends LinearLayout
{
  public PayView(Context context)
  {
    super(context);
    init(null, 0);
  }

  public PayView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init(attrs, 0);
  }

  public PayView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    init(attrs, defStyle);
  }

  private void init(AttributeSet attrs, int defStyle)
  {
  }

}
