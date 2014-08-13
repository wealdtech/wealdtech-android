package com.wealdtech.android.fabric.condition;

/**
 */
public class LongClickViewCondition extends ViewCondition
{
  @Override
  void setUp()
  {
    if (action instanceof LongClickHandler)
    {
      definition.view.setOnLongClickListener(((LongClickHandler)action).getLongClickListener(definition.view));
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
