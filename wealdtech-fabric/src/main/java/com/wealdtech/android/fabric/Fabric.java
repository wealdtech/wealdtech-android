package com.wealdtech.android.fabric;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import com.wealdtech.jackson.WealdMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * A fabric has three scopes of storage:
 * <ul>
 *   <li>Global</li>
 *   <li>Activity</li>
 *   <li>Component</li>
 * </ul>
 * Data can be stored at any of these scopes.  Global scope is persisted through instances of the application,
 * activity scope is persisted through the lifetime of the application and component scope is persisted through
 * the lifetime of the component.
 */
public class Fabric
{
  private static final Logger LOG = LoggerFactory.getLogger(Fabric.class);

  private static Fabric instance = null;

  // Fabric is backed by shared preferences
  private final SharedPreferences prefs;

  // A context is required for creating shared preferences.  This is configured in init()
  private static Context context;

  // A name is required for creating shared preferences.  This is configured in init()
  private static String name;

  // Global scope is contained in a single key:value store
  private final Map<String, Object> globalScope;
  // Activity scope
  private final Map<String, Map<String, Object>> activityScope;
  // Component scope
  private final Map<String, Map<String, Map<String, Object>>> componentScope;

  private Fabric()
  {
    prefs = context.getSharedPreferences("fabric-" + name, Context.MODE_PRIVATE);
    globalScope = Maps.newConcurrentMap();
    activityScope = Maps.newConcurrentMap();
    componentScope = Maps.newConcurrentMap();
  }

  /**
   * Initialise the fabric.
   * @param context the application-level context for the fabric
   * @param name the name for the fabric.
   */
  public static void init(final Context context, final String name)
  {
    Fabric.context = context;
    Fabric.name = name;
  }

  public static Fabric getInstance()
  {
    if (instance == null)
    {
      synchronized (Fabric.class)
      {
        if (context == null)
        {
          throw new IllegalStateException("Fabric has not been initialised; call Fabric.init() before first obtaining fabric");
        }
        if (instance == null)
        {
          instance = new Fabric();
        }
      }
    }
    return instance;
  }

  public <T> T get(final String store, final String key, final Class<T> klazz)
  {
    final String value = prefs.getString(store + ":" + key, null);
    if (value != null)
    {
      try
      {
        return WealdMapper.getMapper().readValue(value, klazz);
      }
      catch (IOException e)
      {
        LOG.error("Failed to parse stored JSON {}: ", value, e);
      }
    }
    return null;
  }

  public <T> void put(final String store, final String key, final T value)
  {
    final SharedPreferences.Editor editor = prefs.edit();
    try
    {
      editor.putString(store + ":" + key, WealdMapper.getMapper().writeValueAsString(value));
    }
    catch (final JsonProcessingException e)
    {
      LOG.error("Failed to store value: ", e);
    }
    editor.commit();
  }

  public void clear(final String store, final String key)
  {
    final SharedPreferences.Editor editor = prefs.edit();
    editor.remove(store + ":" + key);
    editor.commit();
  }

  /**
   * Fetch an item from global scope
   * @param key the key
   * @return the value
   */
  @SuppressWarnings("unchecked")
  public <T> T get(final String key)
  {
    return (T)globalScope.get(key);
  }

  /**
   * Set an item in to global scope
   * @param key the key
   * @param value the value
   */
  public <T> void set(final String key, final T value)
  {
    final SharedPreferences.Editor editor = prefs.edit();
    try
    {
      editor.putString(key, WealdMapper.getMapper().writeValueAsString(value));
    }
    catch (final JsonProcessingException e)
    {
      LOG.error("Failed to set value: ", e);
      throw new IllegalArgumentException("Failed to set value");
    }
    editor.commit();
    globalScope.put(key, value);
  }

  public void clear(final String key)
  {
    final SharedPreferences.Editor editor = prefs.edit();
    editor.remove(key);
    editor.commit();
    globalScope.remove(key);
  }

  /**
   * Fetch an item from activity scope
   * @param key the key
   * @return the value
   */
  @SuppressWarnings("unchecked")
  public <T> T get(final Activity activity, final String key)
  {
    final T result;
    final Map<String, Object> scope = activityScope.get(activity.getLocalClassName());
    if (scope == null)
    {
      result = null;
    }
    else
    {
      final T activityResult = (T)scope.get(key);
      if (activityResult == null)
      {
        result = get(key);
      }
      else
      {
        result = activityResult;
      }
    }
    return result;
  }

  /**
   * Set an item in to activity scope
   * @param activity the activity
   * @param key the key
   * @param value the value
   */
  public <T> void set(final Activity activity, final String key, final T value)
  {
    Map<String, Object> scope = activityScope.get(activity.getLocalClassName());
    if (scope == null)
    {
      scope = Maps.newConcurrentMap();
      activityScope.put(activity.getLocalClassName(), scope);
    }
    scope.put(key, value);
  }

  public void clear(final Activity activity, final String key)
  {
    Map<String, Object> scope = activityScope.get(activity.getLocalClassName());
    if (scope != null)
    {
      scope.remove(key);
    }
  }

  /**
   * Fetch an item from component scope
   * @param activity the activity
   * @param component the component
   * @param key the key
   * @return the value
   */
  @SuppressWarnings("unchecked")
  public <T> T get(final Activity activity, final String component, final String key)
  {
    final T result;
    final Map<String, Map<String, Object>> activityScope = componentScope.get(activity.getLocalClassName());
    if (activityScope == null)
    {
      result = null;
    }
    else
    {
      final Map<String, Object> scope = activityScope.get(component);
      if (scope == null)
      {
        result = null;
      }
      else
      {
        final T componentResult = (T)scope.get(key);
        if (componentResult == null)
        {
          final T activityResult = get(activity, key);
          if (activityResult == null)
          {
            result = get(key);
          }
          else
          {
            result = activityResult;
          }
        }
        else
        {
          result = componentResult;
        }
      }
    }
    return result;
  }

  /**
   * Set an item in to component scope
   * @param activity the activity
   * @param component the component
   * @param key the key
   * @param value the value
   */
  public <T> void set(final Activity activity, final String component, final String key, final T value)
  {
    Map<String, Map<String, Object>> activityScope = componentScope.get(activity.getLocalClassName());
    if (activityScope == null)
    {
      activityScope = Maps.newConcurrentMap();
      componentScope.put(activity.getLocalClassName(), activityScope);
    }
    Map<String, Object> scope = activityScope.get(component);
    if (scope == null)
    {
      scope = Maps.newConcurrentMap();
      activityScope.put(component, scope);
    }

    scope.put(key, value);
  }

  public void clear(final Activity activity, final String component, final String key)
  {
    Map<String, Map<String, Object>> activityScope = componentScope.get(activity.getLocalClassName());
    if (activityScope != null)
    {
      Map<String, Object> scope = activityScope.get(component);
      if (scope != null)
      {
        scope.remove(key);
      }
    }
  }
}
