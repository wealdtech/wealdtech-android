package com.wealdtech.android.fabric.persistence;

import com.wealdtech.android.fabric.Fabric;

/**
 * A persistence store defines how fabric is persisted
 */
public interface FabricPersistenceStore
{
  Fabric load();

  void save(Fabric fabric, String activity, String component, String key);

  Fabric reset();
}
