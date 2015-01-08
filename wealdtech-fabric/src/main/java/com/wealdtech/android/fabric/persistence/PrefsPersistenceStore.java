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

import android.content.Context;
import android.content.SharedPreferences;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.wealdtech.TwoTuple;
import com.wealdtech.android.fabric.Fabric;
import com.wealdtech.jackson.WealdMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A fabric persistence store which uses Jackson and the shared preferences system to store information
 */
public class PrefsPersistenceStore implements FabricPersistenceStore
{
  private static final Logger LOG = LoggerFactory.getLogger(FabricPersistenceStore.class);

  private final SharedPreferences prefs;

  private final ObjectMapper mapper;

  // Single reference to the type of maps we are deserializing
  private static final TypeReference<ConcurrentHashMap<String, Object>> SCOPEREF = new TypeReference<ConcurrentHashMap<String, Object>>(){};

  public PrefsPersistenceStore(final Context context)
  {
    this.prefs = context.getSharedPreferences("fabric", Context.MODE_PRIVATE);
    // Create a custom object mapper which saves type information, allowing complex objects to be stored in prefs
//    this.mapper = WealdMapper.getServerMapper().copy().enableDefaultTyping();
    this.mapper = WealdMapper.getServerMapper();
  }

  @Override
  public Fabric load()
  {
    try
    {
      final String globalScopeStr = prefs.getString("global", null);
      LOG.debug("Global scope is {}", globalScopeStr);
      final Map<String, Object> globalScope;
      if (globalScopeStr == null)
      {
        globalScope = Maps.newHashMap();
      }
      else
      {
        globalScope = mapper.readValue(globalScopeStr, SCOPEREF);
      }

      final String activityScopeStr = prefs.getString("activity", null);
      LOG.debug("Activity scope is {}", activityScopeStr);
      final Map<String, Map<String, Object>> activityScope;
      if (activityScopeStr == null)
      {
        activityScope = Maps.newHashMap();
      }
      else
      {
        activityScope = mapper.readValue(activityScopeStr, SCOPEREF);
      }

      final String componentScopeStr = prefs.getString("component", null);
      LOG.debug("Component scope is {}", componentScopeStr);
      final Map<String, Map<String, Map<String, Object>>> componentScope;
      if (componentScopeStr == null)
      {
        componentScope = Maps.newHashMap();
      }
      else
      {

        componentScope = mapper.readValue(componentScopeStr, SCOPEREF);
      }
      return new Fabric(globalScope, getActivityScopeForFabric(activityScope),
                        getComponentScopeForFabric(componentScope));
    }
    catch (IOException e)
    {
      LOG.error("Failed to instantiate fabric: ", e);
      return reset();
    }
  }

  @Override
  public void save(final Fabric fabric, final String activity, final String component, final String key)
  {
    try
    {
      final SharedPreferences.Editor editor = prefs.edit();

      if (activity == null)
      {
        final Map<String, Object> globalScope = setGlobalScopeForFabricData(fabric.getGlobalScope());
        final String globalScopeStr = mapper.writeValueAsString(globalScope);
        editor.putString("global", globalScopeStr);
        LOG.trace("Global scope is now {}", globalScopeStr);
      }
      else if (component == null)
      {
        final Map<String, Map<String, Object>> activityScope = setActivityScopeForFabricData(fabric.getActivityScope());
        final String activityScopeStr = mapper.writeValueAsString(activityScope);
        editor.putString("activity", activityScopeStr);
        LOG.trace("Activity scope is now {}", activityScopeStr);
      }
      else
      {
        final Map<String, Map<String, Map<String, Object>>> componentScope = setComponentScopeForFabricData(fabric.getComponentScope());
        final String componentScopeStr = mapper.writeValueAsString(componentScope);
        editor.putString("component", componentScopeStr);
        LOG.trace("Component scope is now {}", componentScopeStr);
      }
      editor.commit();
    }
    catch (IOException e)
    {
      LOG.error("Failed to save fabric: ", e);
      throw new IllegalArgumentException("Failed to save fabric", e);
    }
  }

  @Override
  public Fabric reset()
  {
    final SharedPreferences.Editor editor = prefs.edit();
    editor.clear();
    editor.commit();
    return new Fabric(null, null, null);
  }

    /**
     * Convert our local format activity scope data to that suitable for feeding to fabric
     */
    @JsonIgnore
    private Map<String, Map<String, TwoTuple<Object, Boolean>>> getActivityScopeForFabric(final Map<String, Map<String, Object>> activityScope)
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
    private Map<String, Map<String, Map<String, TwoTuple<Object, Boolean>>>> getComponentScopeForFabric(final Map<String, Map<String, Map<String, Object>>> componentScope)
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

  @JsonIgnore
  private Map<String, Object> setGlobalScopeForFabricData(final Map<String, Object> globalScope)
  {
    final Map<String, Object> result = Maps.newHashMap();
    for (final Map.Entry<String, Object> entry : globalScope.entrySet())
    {
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
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
    return result;
  }

  @JsonIgnore
  private Map<String, Map<String, Map<String, Object>>> setComponentScopeForFabricData(final Map<String, Map<String, Map<String, TwoTuple<Object, Boolean>>>> componentScope)
  {
    final Map<String, Map<String, Map<String, Object>>> result = Maps.newHashMap();
    for (final Map.Entry<String, Map<String, Map<String, TwoTuple<Object, Boolean>>>> activityEntry : componentScope.entrySet())
    {
      final Map<String, Map<String, Object>> activityResult = Maps.newHashMap();
      for (final Map.Entry<String, Map<String, TwoTuple<Object, Boolean>>> componentEntry : activityEntry.getValue().entrySet())
      {
        final Map<String, Object> componentResult = Maps.newHashMap();
        for (final Map.Entry<String, TwoTuple<Object, Boolean>> entry : componentEntry.getValue().entrySet())
        {
          if (entry.getValue().getT())
          {
            componentResult.put(entry.getKey(), entry.getValue().getS());
          }
        }
        if (!componentResult.isEmpty())
        {
          activityResult.put(componentEntry.getKey(), componentResult);
        }
      }
      if (!activityResult.isEmpty())
      {
        result.put(activityEntry.getKey(), activityResult);
      }
    }
    return result;
  }
}
