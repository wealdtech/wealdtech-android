package com.wealdtech.android.fabric.condition;

import com.wealdtech.android.fabric.definition.ViewDefinition;

/**
 */
public class LongClickViewCondition extends ViewCondition
{
  @Override
  void setUp()
  {
    if (action instanceof LongClickHandler && definition instanceof ViewDefinition)
    {
      final ViewDefinition definition = (ViewDefinition)this.definition;
      final LongClickHandler action = (LongClickHandler)this.action;
      definition.view.setOnLongClickListener(action.getLongClickListener(definition.view));
    }
  }

  void tearDown()
  {

  }

  public static LongClickViewCondition longClick()
  {
    return new LongClickViewCondition();
  }

}
