package com.wealdtech.android.fabric.condition;

import com.wealdtech.android.fabric.Fabric;
import com.wealdtech.android.fabric.FabricDataListener;
import com.wealdtech.android.fabric.definition.FabricDataDefinition;

/**
 */
public class ChangeFabricDataCondition extends Condition
{
  @Override
  void setUp()
  {
    if (action instanceof FabricDataListener && definition instanceof FabricDataDefinition)
    {
      final FabricDataListener action = (FabricDataListener)this.action;
      final FabricDataDefinition definition = (FabricDataDefinition)this.definition;
      if (definition.data.component.isPresent())
      {
        Fabric.getInstance().addListener(definition.data.activity.get(), definition.data.component.orNull(), definition.data.key, action);
      }
      else if (definition.data.activity.isPresent())
      {
        Fabric.getInstance().addListener(definition.data.activity.get(), definition.data.key, action);
      }
      else
      {
        Fabric.getInstance().addListener(definition.data.key, action);
      }
    }
  }

  public static ChangeFabricDataCondition change()
  {
    return new ChangeFabricDataCondition();
  }
}
