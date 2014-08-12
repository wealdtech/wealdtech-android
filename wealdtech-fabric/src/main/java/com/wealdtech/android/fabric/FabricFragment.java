package com.wealdtech.android.fabric;

import android.support.v4.app.Fragment;

/**
 * A fabric-enabled fragment
 */
public class FabricFragment extends Fragment
{
  protected Fabric fabric(){return Fabric.getInstance();}
}
