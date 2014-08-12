package com.wealdtech.android.fabric;

import android.support.v4.app.FragmentActivity;

/**
 * A fragment activity which uses the fabric framework.
 */
public class FabricFragmentActivity extends FragmentActivity
{
  protected Fabric fabric(){return Fabric.getInstance();}
}
