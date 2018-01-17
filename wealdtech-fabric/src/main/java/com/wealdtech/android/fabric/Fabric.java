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
import android.content.Context;
import android.util.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.wealdtech.TwoTuple;
import com.wealdtech.android.fabric.persistence.FabricPersistenceStore;
import com.wealdtech.android.fabric.persistence.FabricPersistenceStrategy;
import com.wealdtech.android.fabric.persistence.PrefsPersistenceStore;
import com.wealdtech.android.fabric.persistence.SafePersistenceStrategy;
import com.wealdtech.jackson.WealdMapper;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Fabric is an infrastructure component with two goals.  First it acts as a store of information which is globally accessible and
 * can, on a per-item basis, be either transient or persistent.  Second it provides a rules-based way of triggering actions based on
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
  private static final String TAG = Fabric.class.getCanonicalName();

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
    this.globalScope = MoreObjects.firstNonNull(globalScope, Maps.<String, Object>newConcurrentMap());
    this.activityScope = MoreObjects.firstNonNull(activityScope,
                                              Maps.<String, Map<String, TwoTuple<Object, Boolean>>>newConcurrentMap());
    this.componentScope = MoreObjects.firstNonNull(componentScope,
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
    return getInstance(null);
  }

  public static Fabric getInstance(@Nullable final Context context)
  {
    if (instance == null)
    {
      synchronized (Fabric.class)
      {
        if (instance == null)
        {
          if (persistenceStore == null)
          {
            if (context == null)
            {
              throw new IllegalStateException("Fabric has not been initialised; call Fabric.init() before first obtaining fabric");
            }
            else
            {
              persistenceStore = new PrefsPersistenceStore(context);
            }
          }
          if (persistenceStrategy == null)
          {
            persistenceStrategy = new SafePersistenceStrategy(persistenceStore);
          }
          instance = persistenceStore.load();
        }
      }
    }
    return instance;
  }

  private String stringify(final Object val, final boolean isCollection)
  {
    String valStr;
    if (val instanceof String)
    {
      valStr = (String)val;
    }
    else
    {
      try
      {
        valStr = WealdMapper.getMapper().writeValueAsString(val);
      }
      catch (final IOException ioe)
      {
        Log.e(TAG, "Failed to encode value: ", ioe);
        return null;
      }
    }

    if (val instanceof Enum<?>)
    {
      return valStr;
    }

    // TODO need to escape " in the string (but only if in the string - already done?)
    if (!valStr.startsWith("{") && !isCollection && !valStr.startsWith("\""))
    {
      valStr = "\"" + valStr + "\"";
    }
    return valStr;
  }

  @SuppressWarnings("unchecked")
  @Nullable
  private <T> T get(@Nullable final Object obj, final TypeReference<T>typeRef) throws IOException
  {
    if (obj == null)
    {
      return null;
    }

    // Obtain the type we are after through reflection to find out if it is a collection
    final Type type = typeRef.getType() instanceof ParameterizedType ? ((ParameterizedType)typeRef.getType()).getRawType() : typeRef.getType();
    boolean isCollection;
    try
    {
      isCollection = Collection.class.isAssignableFrom(Class.forName(type.toString().replace("class ", "")));
    }
    catch (final ClassNotFoundException cnfe)
    {
      isCollection = false;
    }

    return (T)WealdMapper.getMapper().readValue(stringify(obj, isCollection), typeRef);
  }

  @Nullable
  private <T> T get(@Nullable final Object obj, final Class<T> klazz) throws IOException
  {
    if (obj == null)
    {
      return null;
    }

    final String objStr = stringify(obj, Collection.class.isAssignableFrom(klazz));
    if (objStr == null) {
      return null;
    }
    return WealdMapper.getMapper().readValue(objStr, klazz);
  }

  /**
   * Fetch an item from global scope
   *
   * @param key the key of the item
   *
   * @return the value of the item
   */
  @Nullable
  public <T> T get(final String key, final Class<T> klazz)
  {
    final Object obj = globalScope.get(key);
    try
    {
      return get(obj, klazz);
    }
    catch (final IOException ioe)
    {
      Log.e(TAG, "Failed to obtain " + key + " (" + obj.getClass().getSimpleName() + " as " + klazz + ")");
      Log.e(TAG, "Data is " + obj);
      Log.e(TAG, "Error is ", ioe);
      return null;
    }
  }

  /**
   * Fetch all items from global scope that match the provided pattern
   * @param pattern the pattern
   * @param klazz the class of the items to return
   * @param <T> the class of the items to return
   * @return a map of returned items; possibly empty
   */
  public <T> Map<String, T> getMatching(final String pattern, final Class<T> klazz)
  {
    final Pattern p = Pattern.compile(pattern);
    final Map<String, T> results = new HashMap<>();
    for (final String key : globalScope.keySet())
    {
      if (p.matcher(key).matches())
      {
        results.put(key, get(key, klazz));
      }
    }
    return results;
  }

  /**
   * Fetch an item from global scope
   *
   * @param key the key of the item
   *
   * @return the value of the item
   */
  @Nullable
  public <T> T get(final String key, final TypeReference<T> typeRef)
  {
    final Object obj = globalScope.get(key);
    try
    {
      return get(obj, typeRef);
    }
    catch (final IOException ioe)
    {
      Log.e(TAG, "Failed to obtain " + key + " (" + obj.getClass().getSimpleName() + " as " + typeRef + ")");
      Log.e(TAG, "Data is " + obj);
      Log.e(TAG, "Error is ", ioe);
      return null;
    }
  }

  /**
   * Set an item in global scope
   *
   * @param key the key of the item
   * @param value the value of the item
   */
  public <T> void set(final String key, final T value)
  {
    final T oldValue = (T)get(key, value.getClass());
    globalScope.put(key, value);
    persistenceStrategy.markDirty(key);
    if (!Objects.equal(oldValue, value))
    {
      for (final FabricDataListener<T> listener : MoreObjects.firstNonNull(globalListeners.get(key),
                                                                           ImmutableSet.<FabricDataListener<T>>of()))
      {
        listener.onDataChanged(oldValue, value);
      }
    }
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
      for (final FabricDataListener<T> listener : MoreObjects.firstNonNull(globalListeners.get(key),
                                                                       ImmutableSet.<FabricDataListener<T>>of()))
      {
        listener.onDataChanged(oldValue, null);
      }
    }
  }

  /**
   * Clear multiple items in global scope
   * @param key the key; any items which start with this key will be cleared
   */
  public void clearAll(final String key)
  {
    for (final String entryKey : globalScope.keySet())
    {
      if (entryKey.startsWith(key))
      {
        clear(entryKey);
      }
    }
  }

  private Object getActivityVal(final Activity activity, final String key)
  {
    final Object result;
    final Map<String, TwoTuple<Object, Boolean>> scope = activityScope.get(activity.getLocalClassName());
    if (scope == null)
    {
      result = null;
    }
    else
    {
      final TwoTuple<Object, Boolean> activityResult = scope.get(key);
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
   * Fetch an item from activity scope
   *
   * @param activity the activity for scoping
   * @param key the key of the item
   *
   * @return the value of the item
   */
  @Nullable
  public <T> T get(final Activity activity, final String key, final Class<T> klazz)
  {
    final Object obj = getActivityVal(activity, key);
    try
    {
      return get(obj, klazz);
    }
    catch (final IOException ioe)
    {
      Log.e(TAG, "Failed to obtain " + key + " (" + obj.getClass().getSimpleName() + " as " + klazz + ")");
      Log.e(TAG, "Data is " + obj);
      Log.e(TAG, "Error is ", ioe);
      return null;
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
  @Nullable
  public <T> T get(final Activity activity, final String key, final TypeReference<T> typeRef)
  {
    final Object obj = getActivityVal(activity, key);
    try
    {
      return get(obj, typeRef);
    }
    catch (final IOException ioe)
    {
      Log.e(TAG, "Failed to obtain " + key + " (" + obj.getClass().getSimpleName() + " as " + typeRef + ")");
      Log.e(TAG, "Data is " + obj);
      Log.e(TAG, "Error is ", ioe);
      return null;
    }
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
    if (!Objects.equal(oldValue, value))
    {
      final Multimap<String, FabricDataListener> activityListeners = this.activityListeners.get(activity.getLocalClassName());
      if (activityListeners != null)
      {
        for (final FabricDataListener<T> listener : MoreObjects.firstNonNull(activityListeners.get(key),
                                                                         ImmutableSet.<FabricDataListener<T>>of()))
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
          for (final FabricDataListener<T> listener : MoreObjects.firstNonNull(activityListeners.get(key),
                                                                           ImmutableSet.<FabricDataListener<T>>of()))
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
  @Nullable
  public <T> T get(final Activity activity, final Integer component, final String key, final Class<T> klazz)
  {
    return get(activity, Integer.toString(component), key, klazz);
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
  @Nullable
  public <T> T get(final Activity activity, final Integer component, final String key, final TypeReference<T> typeRef)
  {
    return get(activity, Integer.toString(component), key, typeRef);
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
  public <T> T get(final Activity activity, final String component, final String key, final Class<T> klazz)
  {
    final Object obj = getComponentVal(activity, component, key);
    try
    {
      return get(obj, klazz);
    }
    catch (final IOException ioe)
    {
      Log.e(TAG, "Failed to obtain " + key + " (" + obj.getClass().getSimpleName() + " as " + klazz + ")");
      Log.e(TAG, "Data is " + obj);
      Log.e(TAG, "Error is ", ioe);
      return null;
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
  @SuppressWarnings("unchecked")
  @Nullable
  public <T> T get(final Activity activity, final String component, final String key, final TypeReference<T> typeRef)
  {
    final Object obj = getComponentVal(activity, component, key);
    try
    {
      return get(obj, typeRef);
    }
    catch (final IOException ioe)
    {
      Log.e(TAG, "Failed to obtain " + key + " (" + obj.getClass().getSimpleName() + " as " + typeRef + ")");
      Log.e(TAG, "Data is " + obj);
      Log.e(TAG, "Error is ", ioe);
      return null;
    }
  }

  public Object getComponentVal(final Activity activity, final String component, final String key)
  {
    final Object result;

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
        final TwoTuple<Object, Boolean> activityResult = scope.get(key);
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
    if (!Objects.equal(oldValue, value))
    {
      final Map<String, Multimap<String, FabricDataListener>> activityListeners = this.componentListeners.get(activity.getLocalClassName());
      if (activityListeners != null)
      {
        final Multimap<String, FabricDataListener> componentListeners = activityListeners.get(component);
        if (componentListeners != null)
        {
          for (final FabricDataListener<T> listener : MoreObjects.firstNonNull(componentListeners.get(key),
                                                                           ImmutableSet.<FabricDataListener<T>>of()))
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
              for (final FabricDataListener<T> listener : MoreObjects.firstNonNull(componentListeners.get(key),
                                                                               ImmutableSet.<FabricDataListener<T>>of()))
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

