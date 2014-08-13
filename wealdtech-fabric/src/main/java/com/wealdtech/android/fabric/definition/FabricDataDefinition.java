package com.wealdtech.android.fabric.definition;

import com.wealdtech.android.fabric.FabricData;
import com.wealdtech.android.fabric.condition.FabricDataCondition;

/**
 */
public class FabricDataDefinition
{
  public final FabricData data;

  public FabricDataDefinition(final FabricData data)
  {
    this.data = data;
  }

  public FabricDataCondition encounters(FabricDataCondition condition)
  {
    condition.definition = this;
    return condition;
  }
}
