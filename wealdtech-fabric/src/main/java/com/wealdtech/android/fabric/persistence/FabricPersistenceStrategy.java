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

/**
 * A persistence strategy defines when the in-memory fabric will be persisted
 */
public interface FabricPersistenceStrategy
{
  /**
   * Called when a global scope item is marked as dirty
   */
  void markDirty(String key);

  /**
   * Called when an activity scope item is marked as dirty
   */
  void markDirty(String activity, String key);

  /**
   * Called when a component scope item is marked as dirty
   */
  void markDirty(String activity, String component, String key);
}
