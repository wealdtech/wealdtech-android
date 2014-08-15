package com.wealdtech.android.fabric.trigger;

import android.view.View;
import com.wealdtech.android.fabric.Rule;

/**
 */
public class FocusChangeViewTrigger extends ViewTrigger
{
  private final boolean onGain;
  private final boolean onLoss;

  public FocusChangeViewTrigger(final View view,
                                final boolean onGain,
                                final boolean onLoss)
  {
    super(view);
    this.onGain = onGain;
    this.onLoss = onLoss;
  }

  @Override
  public void setUp(final Rule dta)
  {
    // Need to handle chaining
    final View.OnFocusChangeListener chainedListener = view.getOnFocusChangeListener();

    view.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      @Override
      public void onFocusChange(final View v, final boolean hasFocus)
      {
        if (onGain && hasFocus)
        {
          dta.act();
        }
        else if (onLoss && !hasFocus)
        {
          dta.act();
        }
        if (chainedListener != null)
        {
          chainedListener.onFocusChange(v, hasFocus);
        }
      }
    });
 }

  void tearDown()
  {

  }

  public static FocusChangeViewTrigger focusLost(final View view)
  {
    return new FocusChangeViewTrigger(view, false, true);
  }

  public static FocusChangeViewTrigger focusGain(final View view)
  {
    return new FocusChangeViewTrigger(view, true, false);
  }

  public static FocusChangeViewTrigger focusChange(final View view)
  {
    return new FocusChangeViewTrigger(view, true, true);
  }
}
