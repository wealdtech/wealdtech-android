/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric;

import android.app.Activity;
import android.os.Bundle;
import com.wealdtech.android.fabric.persistence.PrefsPersistenceStore;

/**
 * An activity which uses the fabric framework.
 */
public class FabricActivity extends Activity
{

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    // TODO this is not a good idea as a new persistence store is created each time an activity is created.  This should be in FabricApplication
    Fabric.init(new PrefsPersistenceStore(getApplicationContext()));
 }

  protected Fabric fabric(){return Fabric.getInstance();}
}
