package com.wealdtech.android.fabric.test;

import android.app.Activity;
import android.os.Bundle;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.wealdtech.android.fabric.Fabric;

/**
 */
public class GlobalScopeTestActivity extends Activity
{
  static
  {
    // Only run this once regardless of how many times we start the activity
    BasicLogcatConfigurator.configureDefaultContext();
  }

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Fabric.init(getApplicationContext());
  }
}
