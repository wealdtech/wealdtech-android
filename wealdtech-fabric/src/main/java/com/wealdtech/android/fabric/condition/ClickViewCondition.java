package com.wealdtech.android.fabric.condition;

import com.wealdtech.android.fabric.definition.ViewDefinition;

/**
 */
public class ClickViewCondition extends ViewCondition
{
  @Override
  void setUp()
  {
    if (action instanceof ClickHandler && definition instanceof ViewDefinition)
    {
      final ViewDefinition definition = (ViewDefinition)this.definition;
      final ClickHandler action = (ClickHandler)this.action;

      definition.view.setOnClickListener(action.getClickListener(definition.view));
    }
  }

  void tearDown()
  {

  }

  public static ClickViewCondition click()
  {
    return new ClickViewCondition();
  }
}
