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
