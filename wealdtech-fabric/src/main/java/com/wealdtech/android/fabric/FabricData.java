package com.wealdtech.android.fabric;

import android.app.Activity;
import com.google.common.base.Optional;

/**
 */
public class FabricData
{
  public final Optional<String> activity;
  public final Optional<String> component;
  public final String key;

  private FabricData(final Activity activity, final String component, final String key)
  {
    if (activity == null)
    {
      this.activity = Optional.absent();
    }
    else
    {
      this.activity = Optional.fromNullable(activity.getLocalClassName());
    }
    this.component = Optional.fromNullable(component);
    this.key = key;
  }

  public static FabricData fabricData(final Activity activity, final String key)
  {
    return new FabricData(activity, null, key);
  }

  public static FabricData fabricData(final Activity activity, final int component, final String key)
  {
    return new FabricData(activity, Integer.toString(component), key);
  }

  public static FabricData fabricData(final Activity activity, final String component, final String key)
  {
    return new FabricData(activity, component, key);
  }

  public static FabricData fabricData(final String key)
  {
    return new FabricData(null, null, key);
  }
}
