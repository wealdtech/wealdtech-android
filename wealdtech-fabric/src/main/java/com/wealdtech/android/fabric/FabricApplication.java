package com.wealdtech.android.fabric;

import android.app.Application;

/**
 * An application which uses the fabric framework.
 * This is a simple extension of the standard Application, handling the setup and teardown work for fabric automatically.
 */
public class FabricApplication extends Application
{
  @Override
  public void onCreate()
  {
    super.onCreate();
    Fabric.init(getBaseContext());
  }

  public Fabric fabric()
  {
    return Fabric.getInstance();
  }
}
