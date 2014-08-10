package com.wealdtech.android.fabric.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.wealdtech.android.fabric.Fabric;
import com.wealdtech.jackson.WealdMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A fabric persistence store which
 */
public class PrefsPersistenceStore implements FabricPersistenceStore
{
  private static final Logger LOG = LoggerFactory.getLogger(FabricPersistenceStore.class);

  private final SharedPreferences prefs;

  public PrefsPersistenceStore(final Context context)
  {
    this.prefs = context.getSharedPreferences("fabric", Context.MODE_PRIVATE);
  }

  @Override
  public Fabric load()
  {
    try
    {
      final Fabric fabric = WealdMapper.getServerMapper().readValue(prefs.getString("fabric", "{}"), Fabric.class);
      if (LOG.isTraceEnabled())
      {
        LOG.trace("Fabric is {}", WealdMapper.getServerMapper().writeValueAsString(fabric));
      }
      return fabric;
    }
    catch (IOException e)
    {
      LOG.error("Failed to instantiate fabric: ", e);
      throw new IllegalStateException("Could not load fabric", e);
    }
  }

  @Override
  public void save(final Fabric fabric)
  {
    final SharedPreferences.Editor editor = prefs.edit();
    try
    {
      final String fabricStr = WealdMapper.getServerMapper().writeValueAsString(fabric);
      LOG.trace("Saving fabric state: {}", fabricStr);
      editor.putString("fabric", fabricStr);
    }
    catch (final JsonProcessingException e)
    {
      LOG.error("Failed to save fabric: ", e);
      throw new IllegalArgumentException("Failed to save fabric", e);
    }
    editor.commit();
  }
}
