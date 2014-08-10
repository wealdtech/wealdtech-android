package com.wealdtech.android.fabric.persistence;

import com.wealdtech.android.fabric.Fabric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A persistence strategy which provides maximum safety by saving the
 * fabric whenever it is marked as dirty
 */
public class SafePersistenceStrategy implements FabricPersistenceStrategy
{
  private static final Logger LOG = LoggerFactory.getLogger(SafePersistenceStrategy.class);

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
