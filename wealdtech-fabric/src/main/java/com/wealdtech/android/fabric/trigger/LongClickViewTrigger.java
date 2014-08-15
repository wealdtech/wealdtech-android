package com.wealdtech.android.fabric.trigger;

import android.view.View;
import com.wealdtech.android.fabric.Rule;

/**
 */
public class LongClickViewTrigger extends ViewTrigger
{
  public LongClickViewTrigger(final View view)
  {
    super(view);
  }

  @Override
  public void setUp(final Rule dta)
  {
    view.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(final View v)
      {
        dta.act();
        return false;
      }
    });
 }

  void tearDown()
  {

  }

  public static LongClickViewTrigger longClick(final View view)
  {
    return new LongClickViewTrigger(view);
  }
}
