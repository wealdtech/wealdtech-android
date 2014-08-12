package com.wealdtech.android.fabric;

import android.app.Activity;

/**
 * An activity which uses the fabric framework.
 */
public class FabricActivity extends Activity
{
  protected Fabric fabric(){return Fabric.getInstance();}
}
