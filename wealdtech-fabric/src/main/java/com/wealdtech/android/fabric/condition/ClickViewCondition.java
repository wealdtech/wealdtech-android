package com.wealdtech.android.fabric.condition;

/**
 */
public class ClickViewCondition extends ViewCondition
{
  @Override
  void setUp()
  {
    if (action instanceof ClickHandler)
    {
      definition.view.setOnClickListener(((ClickHandler)action).getClickListener(definition.view));
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
