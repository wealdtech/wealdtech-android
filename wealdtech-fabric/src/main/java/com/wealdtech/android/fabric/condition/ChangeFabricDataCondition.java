package com.wealdtech.android.fabric.condition;

/**
 */
public class ChangeFabricDataCondition extends FabricDataCondition
{
  @Override
  void setUp()
  {
    if (action instanceof DataChangeListener)
    {

    }
  }

  public static ChangeFabricDataCondition change()
  {
    return new ChangeFabricDataCondition();
  }
}
