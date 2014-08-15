package com.wealdtech.android.fabric.trigger;

import com.wealdtech.android.fabric.FabricData;

/**
 * A trigger related to Fabric
 */
public abstract class FabricTrigger extends Trigger
{
  public final FabricData data;

  public FabricTrigger(final FabricData data)
  {
    super();
    this.data = data;
  }
}
