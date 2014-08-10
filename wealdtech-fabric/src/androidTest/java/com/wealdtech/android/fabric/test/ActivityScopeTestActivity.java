package com.wealdtech.android.fabric.test;

import android.app.Activity;
import android.os.Bundle;
import com.wealdtech.android.fabric.Fabric;

/**
 */
public class ActivityScopeTestActivity extends Activity
{
  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Fabric.init(getApplicationContext(), "activityscopetest");
  }
}
