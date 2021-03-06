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
import android.util.Log;
import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.wealdtech.TwoTuple;
import com.wealdtech.android.fabric.persistence.FabricPersistenceStore;
import com.wealdtech.android.fabric.persistence.FabricPersistenceStrategy;
import com.wealdtech.android.fabric.persistence.SafePersistenceStrategy;
import com.wealdtech.jackson.WealdMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Fabric is an infrastructure component with two goals.  First it acts as a store of information which is globally accessible and
 * can, on a per-item basis, e either transient or persistent.  Second it provides a rules-based way of triggering actions based on
 * conditions occurring within an application.  Provided conditions work against both view status and fabric data, but the
 * conditions and actions can be expanded if required.
 * <p/>
 * A fabric has three scopes of storage: <ul> <li>Global</li> <li>Activity</li> <li>Component</li> </ul> Data can be stored at any
 * of these scopes.  Global scope is persisted through instances of the application, activity scope is persisted through the
 * lifetime of the application and component scope is persisted through the lifetime of the component.  Data in activity or
 * component scope which should be persisted past the lifetime of the component can be done so by using the {@code persist()}
 * method.
 * <p/>
 * Note that there is no visibility from one scope to another, so a named variable in global scope will not be returned if that name
 * is asked for in activity scope.  Activity and component scope are purely used for partitioning rather than implying any sort of
 * hierarchy.
 * <p/>
 * The second goal of Fabric is to provide a rule system which allows the definition of reactive actions.  A reactive action occurs
 * based on a condition of current state.  Common examples of this might be showing an introduction overlay for an activity the
 * first time it is visited, validating user input, providing context-sensitive help, or diverting a user to a suggesting changes to user input based on multiple criteria.
 */
public class Fabric
{
  private static final Logger LOG = LoggerFactory.getLogger(Fabric.class);

  private static Fabric instance = null;

  // A persistence strategy and store
  private static FabricPersistenceStore persistenceStore = null;
  private static FabricPersistenceStrategy persistenceStrategy = null;

  // A context is required for creating shared preferences.  This is configured in init()

  // Global scope is contained in a single key:value store
  private final Map<String, Object> globalScope;
  // Activity scope
  private final Map<String, Map<String, TwoTuple<Object, Boolean>>> activityScope;
  // Component scope
  private final Map<String, Map<String, Map<String, TwoTuple<Object, Boolean>>>> componentScope;

  // Listeners for fabric data
  private final Multimap<String, FabricDataListener> globalListeners;
  private final Map<String, Multimap<String, FabricDataListener>> activityListeners;
  private final Map<String, Map<String, Multimap<String, FabricDataListener>>> componentListeners;

  /**
   * Create a new fabric.
   */
  public Fabric(final Map<String, Object> globalScope,
                final Map<String, Map<String, TwoTuple<Object, Boolean>>> activityScope,
                final Map<String, Map<String, Map<String, TwoTuple<Object, Boolean>>>> componentScope)
  {
    this.globalScope = Objects.firstNonNull(globalScope, Maps.<String, Object>newConcurrentMap());
    this.activityScope = Objects.firstNonNull(activityScope,
                                              Maps.<String, Map<String, TwoTuple<Object, Boolean>>>newConcurrentMap());
    this.componentScope = Objects.firstNonNull(componentScope,
                                               Maps.<String, Map<String, Map<String, TwoTuple<Object, Boolean>>>>newConcurrentMap());
    this.globalListeners = ArrayListMultimap.create();
    this.activityListeners = Maps.newConcurrentMap();
    this.componentListeners = Maps.newConcurrentMap();
  }

  /**
   * Initialise the fabric.
   *
   * @param persistenceStore the persistence store for the fabric
   */
  public static void init(final FabricPersistenceStore persistenceStore)
  {
    Fabric.persistenceStore = persistenceStore;
  }

  public static void setPersistenceStrategy(final FabricPersistenceStrategy persistenceStrategy)
  {
    Fabric.persistenceStrategy = persistenceStrategy;
  }

  public Map<String, Object> getGlobalScope()
  {
    return this.globalScope;
  }

  public Map<String, Map<String, TwoTuple<Object, Boolean>>> getActivityScope()
  {
    return this.activityScope;
  }

  public Map<String, Map<String, Map<String, TwoTuple<Object, Boolean>>>> getComponentScope()
  {
    return this.componentScope;
  }

  public static Fabric getInstance()
  {
    if (instance == null)
    {
      synchronized (Fabric.class)
      {
        if (instance == null)
        {
          if (persistenceStore == null)
          {
            throw new IllegalStateException("Fabric has not been initialised; call Fabric.init() before first obtaining fabric");
          }
          if (persistenceStrategy == null)
          {
            persistenceStrategy = new SafePersistenceStrategy(persistenceStore);
          }
          instance = persistenceStore.load();
          if (LOG.isTraceEnabled())
          {
            try
            {
              LOG.trace("Fabric is {}", WealdMapper.getServerMapper().writeValueAsString(instance));
            }
            catch (Exception e)
            {
              LOG.error("Failed to instantiate fabric: ", e);
            }
          }
        }
      }
    }
    return instance;
  }

  /**
   * Fetch an item from global scope
   *
   * @param key the key of the item
   *
   * @return the value of the item
   */
  @SuppressWarnings("unchecked")
  public <T> T get(final String key)
  {
    return (T)globalScope.get(key);
  }

  /**
   * Set an item in global scope
   *
   * @param key the key of the item
   * @param value the value of the item
   */
  public <T> void set(final String key, final T value)
  {
    final T oldValue = (T)globalScope.put(key, value);
    persistenceStrategy.markDirty(key);
    if (!equalTo(oldValue, value))
    {
      for (final FabricDataListener<T> listener : Objects.firstNonNull(globalListeners.get(key),
                                                                       ImmutableSet.<FabricDataListener>of()))
      {
        listener.onDataChanged(oldValue, value);
      }
    }
  }

  /**
   * Null-aware equals()
   */
  private <T> boolean equalTo(T left, T right)
  {
    if (left == null && right == null)
    {
      return true;
    }
    if (left == null || right == null)
    {
      return false;
    }
    return left.equals(right);
  }

  /**
   * Clear an item in global scope
   *
   * @param key the key of the item
   */
  public <T> void clear(final String key)
  {
    final T oldValue = (T)globalScope.remove(key);
    persistenceStrategy.markDirty(key);
    if (oldValue != null)
    {
      for (final FabricDataListener<T> listener : Objects.firstNonNull(globalListeners.get(key),
                                                                       ImmutableSet.<FabricDataListener>of()))
      {
        listener.onDataChanged(oldValue, null);
      }
    }
  }

  /**
   * Fetch an item from activity scope
   *
   * @param activity the activity for scoping
   * @param key the key of the item
   *
   * @return the value of the item
   */
  @SuppressWarnings("unchecked")
  @Nullable
  public <T> T get(final Activity activity, final String key)
  {
    final T result;

    final Map<String, TwoTuple<Object, Boolean>> scope = activityScope.get(activity.getLocalClassName());
    if (scope == null)
    {
      result = null;
    }
    else
    {
      final TwoTuple<T, Boolean> activityResult = (TwoTuple<T, Boolean>)scope.get(key);
      if (activityResult == null)
      {
        result = null;
      }
      else
      {
        result = activityResult.getS();
      }
    }

    return result;
  }

  /**
   * Set an item in activity scope
   *
   * @param activity the activity for scoping
   * @param key the key of the item
   * @param value the value of the item
   */
  public <T> void set(final Activity activity, final String key, final T value)
  {
    Map<String, TwoTuple<Object, Boolean>> scope = activityScope.get(activity.getLocalClassName());
    if (scope == null)
    {
      scope = Maps.newConcurrentMap();
      activityScope.put(activity.getLocalClassName(), scope);
    }
    final TwoTuple<Object, Boolean> oldEntry = scope.get(key);
    if (oldEntry == null || !oldEntry.getT())
    {
      scope.put(key, new TwoTuple<Object, Boolean>(value, false));
    }
    else
    {
      scope.put(key, new TwoTuple<Object, Boolean>(value, true));
      persistenceStrategy.markDirty(activity.getLocalClassName(), key);
    }
    final T oldValue = oldEntry == null ? null : (T)oldEntry.getS();
    if (!equalTo(oldValue, value))
    {
      final Multimap<String, FabricDataListener> activityListeners = this.activityListeners.get(activity.getLocalClassName());
      if (activityListeners != null)
      {
        for (final FabricDataListener<T> listener : Objects.firstNonNull(activityListeners.get(key),
                                                                         ImmutableSet.<FabricDataListener>of()))
        {
          listener.onDataChanged(oldValue, value);
        }
      }
    }
  }

  /**
   * Clear an item in activity scope
   *
   * @param activity the activity for scoping
   * @param key the key of the item
   */
  public <T> void clear(final Activity activity, final String key)
  {
    Map<String, TwoTuple<Object, Boolean>> scope = activityScope.get(activity.getLocalClassName());
    if (scope != null)
    {
      final TwoTuple<Object, Boolean> oldEntry = scope.remove(key);
      if (oldEntry != null && oldEntry.getT())
      {
        // This was persisted, so we need to mark as dirty
        persistenceStrategy.markDirty(activity.getLocalClassName(), key);
      }
      final T oldValue = oldEntry == null ? null : (T)oldEntry.getS();
      if (oldValue != null)
      {
        final Multimap<String, FabricDataListener> activityListeners = this.activityListeners.get(activity.getLocalClassName());
        if (activityListeners != null)
        {
          for (final FabricDataListener<T> listener : Objects.firstNonNull(activityListeners.get(key),
                                                                           ImmutableSet.<FabricDataListener>of()))
          {
            listener.onDataChanged(oldValue, null);
          }
        }
      }
    }
  }

  /**
   * Mark an activity-level item to be persisted
   *
   * @param activity the activity for scoping
   * @param key the key of the item
   */
  public void persist(final Activity activity, final String key)
  {
    final Map<String, TwoTuple<Object, Boolean>> scope = activityScope.get(activity.getLocalClassName());
    if (scope == null)
    {
      throw new IllegalStateException("Attempt to persist a non-existent item");
    }
    final TwoTuple<Object, Boolean> value = scope.get(key);
    if (value == null)
    {
      throw new IllegalStateException("Attempt to persist a non-existent item");
    }
    if (!value.getT())
    {
      // This item was not persisted but now is.  Set it accordingly
      scope.put(key, new TwoTuple<>(value.getS(), true));
      persistenceStrategy.markDirty(activity.getLocalClassName(), key);
    }
  }

  /**
   * Mark an activity-level item to be unpersisted
   *
   * @param activity the activity for scoping
   * @param key the key of the item
   */
  public void unpersist(final Activity activity, final String key)
  {
    final Map<String, TwoTuple<Object, Boolean>> scope = activityScope.get(activity.getLocalClassName());
    if (scope == null)
    {
      throw new IllegalStateException("Attempt to unpersist a non-existent item");
    }
    final TwoTuple<Object, Boolean> value = scope.get(key);
    if (value == null)
    {
      throw new IllegalStateException("Attempt to unpersist a non-existent item");
    }
    if (value.getT())
    {
      // This item was persisted but now is not.  Set it accordingly
      scope.put(key, new TwoTuple<>(value.getS(), false));
      // We mark as dirty because of the change of persistence state
      persistenceStrategy.markDirty(activity.getLocalClassName(), key);
    }
  }

  /**
   * Fetch an item from component scope
   *
   * @param activity the activity for scoping
   * @param component the component for scoping
   * @param key the key of the item
   *
   * @return the value of the item
   */
  public <T> T get(final Activity activity, final Integer component, final String key)
  {
    return get(activity, Integer.toString(component), key);
  }

  /**
   * Fetch an item from component scope
   *
   * @param activity the activity for scoping
   * @param component the component for scoping
   * @param key the key of the item
   *
   * @return the value of the item
   */
  @SuppressWarnings("unchecked")
  @Nullable
  public <T> T get(final Activity activity, final String component, final String key)
  {
    final T result;

    final Map<String, Map<String, TwoTuple<Object, Boolean>>> activityScope = componentScope.get(activity.getLocalClassName());
    if (activityScope == null)
    {
      result = null;
    }
    else
    {
      final Map<String, TwoTuple<Object, Boolean>> scope = activityScope.get(component);
      if (scope == null)
      {
        result = null;
      }
      else
      {
        final TwoTuple<T, Boolean> activityResult = (TwoTuple<T, Boolean>)scope.get(key);
        if (activityResult == null)
        {
          result = null;
        }
        else
        {
          result = activityResult.getS();
        }
      }
    }

    return result;
  }

  /**
   * Set an item in component scope
   *
   * @param activity the activity for scoping
   * @param component the component for scoping
   * @param key the key of the item
   * @param value the value of the item
   */
  public <T> void set(final Activity activity, final Integer component, final String key, final T value)
  {
    set(activity, Integer.toString(component), key, value);
  }

  /**
   * Set an item in component scope
   *
   * @param activity the activity for scoping
   * @param component the component for scoping
   * @param key the key of the item
   * @param value the value of the item
   */
  public <T> void set(final Activity activity, final String component, final String key, final T value)
  {
    Map<String, Map<String, TwoTuple<Object, Boolean>>> activityScope = componentScope.get(activity.getLocalClassName());
    if (activityScope == null)
    {
      activityScope = Maps.newConcurrentMap();
      componentScope.put(activity.getLocalClassName(), activityScope);
    }
    Map<String, TwoTuple<Object, Boolean>> scope = activityScope.get(component);
    if (scope == null)
    {
      scope = Maps.newConcurrentMap();
      activityScope.put(component, scope);
    }

    final TwoTuple<Object, Boolean> oldEntry = scope.put(key, new TwoTuple<Object, Boolean>(value, false));
    // FIXME persistence

    final T oldValue = oldEntry == null ? null : (T)oldEntry.getS();
    if (!equalTo(oldValue, value))
    {
      final Map<String, Multimap<String, FabricDataListener>> activityListeners = this.componentListeners.get(activity.getLocalClassName());
      if (activityListeners != null)
      {
        final Multimap<String, FabricDataListener> componentListeners = activityListeners.get(component);
        if (componentListeners != null)
        {
          for (final FabricDataListener<T> listener : Objects.firstNonNull(componentListeners.get(key),
                                                                           ImmutableSet.<FabricDataListener>of()))
          {
            listener.onDataChanged(oldValue, value);
          }
        }
      }
    }
  }

  /**
   * Clear an item in component scope
   *
   * @param activity the activity for scoping
   * @param component the component for scoping
   * @param key the key of the item
   */

  public <T> void clear(final Activity activity, final Integer component, final String key)
  {
    clear(activity, Integer.toString(component), key);
  }

  /**
   * Clear an item in component scope
   *
   * @param activity the activity for scoping
   * @param component the component for scoping
   * @param key the key of the item
   */
  public <T> void clear(final Activity activity, final String component, final String key)
  {
    Map<String, Map<String, TwoTuple<Object, Boolean>>> activityScope = componentScope.get(activity.getLocalClassName());
    if (activityScope != null)
    {
      Map<String, TwoTuple<Object, Boolean>> scope = activityScope.get(component);
      if (scope != null)
      {
        final TwoTuple<Object, Boolean> oldEntry = scope.remove(key);
        // FIXME persistence
        final T oldValue = oldEntry == null ? null : (T)oldEntry.getS();
        if (oldValue != null)
        {
          final Map<String, Multimap<String, FabricDataListener>> activityListeners = this.componentListeners.get(activity.getLocalClassName());
          if (activityListeners != null)
          {
            final Multimap<String, FabricDataListener> componentListeners = activityListeners.get(component);
            if (componentListeners != null)
            {
              for (final FabricDataListener<T> listener : Objects.firstNonNull(componentListeners.get(key),
                                                                               ImmutableSet.<FabricDataListener>of()))
              {
                listener.onDataChanged(oldValue, null);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Mark a component-level item to be persisted
   *
   * @param activity the activity for scoping
   * @param component the component for scoping
   * @param key the key of the item
   */
  public void persist(final Activity activity, final Integer component, final String key)
  {
    persist(activity, Integer.toString(component), key);
  }

  /**
   * Mark a component-level item to be persisted
   *
   * @param activity the activity for scoping
   * @param component the component for scoping
   * @param key the key of the item
   */
  public void persist(final Activity activity, final String component, final String key)
  {
    final Map<String, Map<String, TwoTuple<Object, Boolean>>> activityScope = componentScope.get(activity.getLocalClassName());
    if (activityScope == null)
    {
      throw new IllegalStateException("Attempt to persist a non-existent item");
    }
    final Map<String, TwoTuple<Object, Boolean>> scope = activityScope.get(component);
    if (scope == null)
    {
      throw new IllegalStateException("Attempt to persist a non-existent item");
    }
    final TwoTuple<Object, Boolean> value = scope.get(key);
    if (value == null)
    {
      throw new IllegalStateException("Attempt to persist a non-existent item");
    }
    if (!value.getT())
    {
      // This item was not persisted but now is.  Set it accordingly
      scope.put(key, new TwoTuple<>(value.getS(), true));
      persistenceStrategy.markDirty(activity.getLocalClassName(), component, key);
    }
  }

  /**
   * Mark a component-level item to be unpersisted
   *
   * @param activity the activity for scoping
   * @param component the component for scoping
   * @param key the key of the item
   */
  public void unpersist(final Activity activity, final Integer component, final String key)
  {
    unpersist(activity, Integer.toString(component), key);
  }

  /**
   * Mark a component-level item to be unpersisted
   *
   * @param activity the activity for scoping
   * @param component the component for scoping
   * @param key the key of the item
   */
  public void unpersist(final Activity activity, final String component, final String key)
  {
    final Map<String, Map<String, TwoTuple<Object, Boolean>>> activityScope = componentScope.get(activity.getLocalClassName());
    if (activityScope == null)
    {
      Log.e("Fabric", "Current component-level scope is " + componentScope.toString());
      throw new IllegalStateException("Attempt to unpersist a non-existent item " + activity.getLocalClassName() + ":" + component + ":" + key);
    }
    final Map<String, TwoTuple<Object, Boolean>> scope = activityScope.get(component);
    if (scope == null)
    {
      Log.e("Fabric", "Current component-level scope is " + componentScope.toString());
      throw new IllegalStateException("Attempt to unpersist a non-existent item " + activity.getLocalClassName() + ":" + component + ":" + key);
    }
    final TwoTuple<Object, Boolean> value = scope.get(key);
    if (value == null)
    {
      Log.e("Fabric", "Current component-level scope is " + componentScope.toString());
      throw new IllegalStateException("Attempt to unpersist a non-existent item " + activity.getLocalClassName() + ":" + component + ":" + key);
    }
    if (value.getT())
    {
      // This item was persisted but now is not.  Set it accordingly
      scope.put(key, new TwoTuple<>(value.getS(), false));
      // We mark as dirty because of the change of persistence state
      persistenceStrategy.markDirty(activity.getLocalClassName(), component, key);
    }
  }

  public <T> void addListener(final String key, final FabricDataListener<T> listener)
  {
    globalListeners.put(key, listener);
  }

  public <T> void addListener(final Activity activity, final String key, final FabricDataListener<T> listener)
  {
    addListener(activity.getLocalClassName(), key, listener);
  }

  public <T> void addListener(final String activity, final String key, final FabricDataListener<T> listener)
  {
    // Obtain current List of listeners
    Multimap<String, FabricDataListener> listeners = activityListeners.get(activity);
    if (listeners == null)
    {
      listeners = ArrayListMultimap.create();
      activityListeners.put(activity, listeners);
    }
    listeners.put(key, listener);
  }

  public <T> void addListener(final Activity activity, final int component, final String key, final FabricDataListener<T> listener)
  {
    addListener(activity.getLocalClassName(), Integer.toString(component), key, listener);
  }

  public <T> void addListener(final Activity activity,
                              final String component,
                              final String key,
                              final FabricDataListener<T> listener)
  {
    addListener(activity.getLocalClassName(), component, key, listener);
  }

  public <T> void addListener(final String activity, final String component, final String key, final FabricDataListener<T> listener)
  {
    // Obtain current List of listeners
    Map<String, Multimap<String, FabricDataListener>> activityListeners = componentListeners.get(activity);
    if (activityListeners == null)
    {
      activityListeners = Maps.newConcurrentMap();
      componentListeners.put(activity, activityListeners);
    }
    Multimap<String, FabricDataListener> listeners = activityListeners.get(component);
    if (listeners == null)
    {
      listeners = ArrayListMultimap.create();
      activityListeners.put(component, listeners);
    }
    listeners.put(key, listener);
  }
}

