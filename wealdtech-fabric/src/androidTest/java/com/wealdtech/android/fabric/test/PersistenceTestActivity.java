/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric.test;

import android.app.Activity;
import android.os.Bundle;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.wealdtech.android.fabric.Fabric;
import com.wealdtech.android.fabric.persistence.PrefsPersistenceStore;

/**
 */
public class PersistenceTestActivity extends Activity
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
    Fabric.init(new PrefsPersistenceStore(getApplicationContext()));
  }
}
