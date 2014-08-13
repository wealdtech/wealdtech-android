package com.wealdtech.android.fabric.condition;

/**
 */
public class FocusChangeViewCondition extends ViewCondition
{
  @Override
  void setUp()
  {
    if (action instanceof FocusChangeHandler)
    {
      definition.view.setOnFocusChangeListener(((FocusChangeHandler)action).getFocusChangeListener(definition.view));
    }
  }

  void tearDown()
  {

  }

  public static FocusChangeViewCondition focusChange()
  {
    return new FocusChangeViewCondition();
  }
}
