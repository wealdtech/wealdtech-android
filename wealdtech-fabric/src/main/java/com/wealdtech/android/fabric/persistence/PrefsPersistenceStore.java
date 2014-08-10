package com.wealdtech.android.fabric.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.wealdtech.TwoTuple;
import com.wealdtech.android.fabric.Fabric;
import com.wealdtech.jackson.WealdMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * A fabric persistence store which uses Jackson and the shared preferences system to store information
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
      LOG.error("Fabric data is {}", prefs.getString("fabric", "{}"));
      final FabricData fabricData = WealdMapper.getServerMapper().readValue(prefs.getString("fabric", "{}"), FabricData.class);
      if (LOG.isTraceEnabled())
      {
        LOG.trace("Fabric data is {}", WealdMapper.getServerMapper().writeValueAsString(fabricData));
      }
      return new Fabric(fabricData.globalScope, fabricData.getActivityScopeForFabric(), fabricData.getComponentScopeForFabric());
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
    final FabricData fabricData = new FabricData(fabric.getGlobalScope(),
                                                 setActivityScopeForFabricData(fabric.getActivityScope()),
                                                 null);
//                                                 FabricData.setComponentScopeForFabricData(fabric.getComponentScope()));

    final SharedPreferences.Editor editor = prefs.edit();
    try
    {
      final String fabricDataStr = WealdMapper.getServerMapper().writeValueAsString(fabricData);
      LOG.trace("Saving fabric data: {}", fabricDataStr);
      editor.putString("fabric", fabricDataStr);
    }
    catch (final JsonProcessingException e)
    {
      LOG.error("Failed to save fabric: ", e);
      throw new IllegalArgumentException("Failed to save fabric", e);
    }
    editor.commit();
  }

  // Helper class used for loading and saving the fabric data
  private class FabricData
  {
    // Global scope
    public final Map<String, Object> globalScope;
    // Persistent activity scope data
    public final Map<String, Map<String, Object>> activityScope;
    // Persistent component scope data
    public final Map<String, Map<String, Map<String, Object>>> componentScope;

    @JsonCreator
    private FabricData(@JsonProperty("globalscope") final Map<String, Object> globalScope,
                       @JsonProperty("activityscope") final Map<String, Map<String, Object>> activityScope,
                       @JsonProperty("componentscope") final Map<String, Map<String, Map<String, Object>>> componentScope)
    {
      this.globalScope = Objects.firstNonNull(globalScope, Maps.<String, Object>newConcurrentMap());
      this.activityScope = Objects.firstNonNull(activityScope, Maps.<String, Map<String, Object>>newConcurrentMap());
      this.componentScope = Objects.firstNonNull(componentScope, Maps.<String, Map<String, Map<String, Object>>>newConcurrentMap());
    }

    /**
     * Convert our local format activity scope data to that suitable for feeding to fabric
     */
    @JsonIgnore
    private Map<String, Map<String, TwoTuple<Object, Boolean>>> getActivityScopeForFabric()
    {
      // By definition, anything we have is persistent.  Set it as such
      Map<String, Map<String, TwoTuple<Object, Boolean>>> fabricActivityScope = Maps.newHashMap();
      for (final Map.Entry<String, Map<String, Object>> activityEntry : activityScope.entrySet())
      {
        Map<String, TwoTuple<Object, Boolean>> activityResult = Maps.newHashMap();
        for (final Map.Entry<String, Object> entry : activityEntry.getValue().entrySet())
        {
          activityResult.put(entry.getKey(), new TwoTuple<>(entry.getValue(), true));
        }
        fabricActivityScope.put(activityEntry.getKey(), activityResult);
      }
      return fabricActivityScope;
    }

    /**
     * Convert our local format component scope data to that suitable for feeding to fabric
     */
    @JsonIgnore
    private Map<String, Map<String, Map<String, TwoTuple<Object, Boolean>>>> getComponentScopeForFabric()
    {
      // By definition, anything we have loaded is persistent.  Set it as such
      Map<String, Map<String, Map<String, TwoTuple<Object, Boolean>>>> persistentComponentScope = Maps.newHashMap();
      for (final Map.Entry<String, Map<String, Map<String, Object>>> activityEntry : componentScope.entrySet())
      {
        Map<String, Map<String, TwoTuple<Object, Boolean>>> activityResult = Maps.newHashMap();
        for (final Map.Entry<String, Map<String, Object>> componentEntry : activityEntry.getValue().entrySet())
        {
          Map<String, TwoTuple<Object, Boolean>> componentResult = Maps.newHashMap();
          for (final Map.Entry<String, Object> entry : componentEntry.getValue().entrySet())
          {
            componentResult.put(entry.getKey(), new TwoTuple<>(entry.getValue(), true));
          }
          activityResult.put(componentEntry.getKey(), componentResult);
        }
        persistentComponentScope.put(activityEntry.getKey(), activityResult);
      }
      return persistentComponentScope;
    }
  }

  @JsonIgnore
  private Map<String, Map<String, Object>> setActivityScopeForFabricData(final Map<String, Map<String, TwoTuple<Object, Boolean>>> activityScope)
  {
    final Map<String, Map<String, Object>> result = Maps.newHashMap();
    for (final Map.Entry<String, Map<String, TwoTuple<Object, Boolean>>> activityEntry : activityScope.entrySet())
    {
      final Map<String, Object> activityResult = Maps.newHashMap();
      for (final Map.Entry<String, TwoTuple<Object, Boolean>> entry : activityEntry.getValue().entrySet())
      {
        if (entry.getValue().getT())
        {
          activityResult.put(entry.getKey(), entry.getValue().getS());
        }
      }
      if (!activityResult.isEmpty())
      {
        result.put(activityEntry.getKey(), activityResult);
      }
    }
    if (!result.isEmpty())
    {
      return result;
    }
    else
    {
      return null;
    }
  }
}
