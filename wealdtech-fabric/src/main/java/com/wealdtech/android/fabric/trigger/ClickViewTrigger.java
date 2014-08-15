package com.wealdtech.android.fabric.trigger;

import android.view.View;
import com.wealdtech.android.fabric.Rule;

/**
 */
public class ClickViewTrigger extends ViewTrigger
{
  public ClickViewTrigger(final View view)
  {
    super(view);
  }

  @Override
  public void setUp(final Rule dta)
  {
    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v)
      {
        dta.act();
      }
    });
 }

  void tearDown()
  {

  }

  public static ClickViewTrigger click(final View view)
  {
    return new ClickViewTrigger(view);
  }
}
