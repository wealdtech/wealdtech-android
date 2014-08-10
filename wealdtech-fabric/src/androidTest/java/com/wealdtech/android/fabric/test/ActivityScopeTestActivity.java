package com.wealdtech.android.fabric.test;

import android.app.Activity;
import android.os.Bundle;
import com.wealdtech.android.fabric.Fabric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class ActivityScopeTestActivity extends Activity
{
  private static final Logger LOG = LoggerFactory.getLogger(ActivityScopeTestActivity.class);

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Fabric.init(getApplicationContext());
  }
}
