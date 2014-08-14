package com.wealdtech.android.fabric.condition;

import com.wealdtech.android.fabric.definition.ViewDefinition;

/**
 */
public class FocusChangeViewCondition extends ViewCondition
{
  @Override
  void setUp()
  {
    if (action instanceof FocusChangeHandler && definition instanceof ViewDefinition)
    {
      final ViewDefinition definition = (ViewDefinition)this.definition;
      final FocusChangeHandler action = (FocusChangeHandler)this.action;

      action.setChainedFocusChangeListener(definition.view.getOnFocusChangeListener());
      definition.view.setOnFocusChangeListener(action.getFocusChangeListener());
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
