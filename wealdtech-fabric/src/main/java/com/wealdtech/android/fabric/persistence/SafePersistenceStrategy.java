/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric.persistence;

import com.wealdtech.android.fabric.Fabric;

/**
 * A persistence strategy which provides maximum safety by saving the
 * fabric whenever it is marked as dirty
 */
public class SafePersistenceStrategy implements FabricPersistenceStrategy
{
  private final FabricPersistenceStore store;

  public SafePersistenceStrategy(final FabricPersistenceStore store)
  {
    this.store = store;
  }

  @Override
  public void markDirty(final String key)
  {
    this.store.save(Fabric.getInstance(), null, null, key);
  }

  @Override
  public void markDirty(final String activity, final String key)
  {
    this.store.save(Fabric.getInstance(), activity, null, key);
  }

  @Override
  public void markDirty(final String activity, final String component, final String key)
  {
    this.store.save(Fabric.getInstance(), activity, component, key);
  }
}
