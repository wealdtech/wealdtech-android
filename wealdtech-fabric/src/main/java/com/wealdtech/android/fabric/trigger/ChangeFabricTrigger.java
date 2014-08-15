package com.wealdtech.android.fabric.trigger;

import com.wealdtech.android.fabric.Rule;
import com.wealdtech.android.fabric.Fabric;
import com.wealdtech.android.fabric.FabricData;
import com.wealdtech.android.fabric.FabricDataListener;

/**
 */
public class ChangeFabricTrigger extends FabricTrigger implements FabricDataListener
{
  // TODO this is not a good idea; how do we avoid this?
  private Rule dta;

  public ChangeFabricTrigger(final FabricData data)
  {
    super(data);
  }

  @Override
  public void setUp(final Rule dta)
  {
    this.dta = dta;
    if (data.component.isPresent())
    {
      Fabric.getInstance().addListener(data.activity.get(), data.component.orNull(), data.key, this);
    }
    else if (data.activity.isPresent())
    {
      Fabric.getInstance().addListener(data.activity.get(), data.key, this);
    }
    else
    {
      Fabric.getInstance().addListener(data.key, this);
    }
  }

  @Override
  public void onDataChanged(final Object oldData, final Object newData)
  {
    dta.act();
  }

  public static ChangeFabricTrigger change(final FabricData data)
  {
    return new ChangeFabricTrigger(data);
  }
}
