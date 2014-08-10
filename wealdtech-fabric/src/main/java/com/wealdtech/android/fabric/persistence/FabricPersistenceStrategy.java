package com.wealdtech.android.fabric.persistence;

/**
 * A persistence strategy defines when the in-memory fabric will be persisted
 */
public interface FabricPersistenceStrategy
{
  void markDirty();
}
