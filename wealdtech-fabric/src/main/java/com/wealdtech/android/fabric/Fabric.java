package com.wealdtech.android.fabric;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
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
 * the lifetime of the component.  Data in activity or component scope which should be persisted past the lifetime
 * of the component can be done so by using the {@code persist} method
 */
public class Fabric
{
  private static final Logger LOG = LoggerFactory.getLogger(Fabric.class);

  private static Fabric instance = null;

  // Fabric is backed by shared preferences
  @JsonIgnore
  private final SharedPreferences prefs;

  // A context is required for creating shared preferences.  This is configured in init()
  @JsonIgnore
  private static Context context;

  // A name is required for creating shared preferences.  This is configured in init()
  @JsonIgnore
  private static String name;

  // Global scope is contained in a single key:value store
  private final Map<String, Object> globalScope;
  // Activity scope
  private final Map<String, Map<String, Object>> activityScope;
  private final Multimap<String, String> activityPersists;
  // Component scope
  private final Map<String, Map<String, Map<String, Object>>> componentScope;
//  private final Map<String, Map<String, String>> componentPersists;

  @JsonCreator
  private Fabric(@JsonProperty("globalscope") final Map<String, Object> globalScope,
                 @JsonProperty("activityscope") final Map<String, Map<String, Object>> activityScope,
                 @JsonProperty("activitypersists") final Multimap<String, String> activityPersists,
                 @JsonProperty("componentscope") final Map<String, Map<String, Map<String, Object>>> componentScope)
  {
    this.prefs = context.getSharedPreferences("fabric-" + name, Context.MODE_PRIVATE);
    this.globalScope = Objects.firstNonNull(globalScope, Maps.<String, Object>newConcurrentMap());
    this.activityScope = Objects.firstNonNull(activityScope, Maps.<String, Map<String, Object>>newConcurrentMap());
    this.activityPersists = Objects.firstNonNull(activityPersists, ArrayListMultimap.<String, String>create());
    this.componentScope = Objects.firstNonNull(componentScope, Maps.<String, Map<String, Map<String, Object>>>newConcurrentMap());
  }
//  private Fabric()
//  {
//    prefs = context.getSharedPreferences("fabric-" + name, Context.MODE_PRIVATE);
//    globalScope = Maps.newConcurrentMap();
//    globalScope.putAll(prefs.getAll());
//    activityScope = Maps.newConcurrentMap();
//    // Import activity persists from preferences
//    activityPersists = ArrayListMultimap.create();
//    // Import existing activity scope data from preferences
//    componentScope = Maps.newConcurrentMap();
//    // Read component persists from preferences
//    // Import existing component scope data
//  }

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
          final SharedPreferences prefs = context.getSharedPreferences("fabric-" + name, Context.MODE_PRIVATE);
          try
          {
            instance = WealdMapper.getServerMapper().readValue(prefs.getString("fabric", "{}"), Fabric.class);
            LOG.error("Fabric is {}", WealdMapper.getServerMapper().writeValueAsString(instance));
          }
          catch (IOException e)
          {
            LOG.error("Failed to instantiate fabric: ", e);
          }
        }
      }
    }
    return instance;
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
   * Set an item in global scope
   * @param key the key
   * @param value the value
   */
  public <T> void set(final String key, final T value)
  {
//    final SharedPreferences.Editor editor = prefs.edit();
//    try
//    {
//      editor.putString(key, WealdMapper.getMapper().writeValueAsString(value));
//    }
//    catch (final JsonProcessingException e)
//    {
//      LOG.error("Failed to set value {}: ", value, e);
//      throw new IllegalArgumentException("Failed to set value");
//    }
//    editor.commit();
    globalScope.put(key, value);
    saveState();
  }

  /**
   * Clear an item in global scope
   * @param key the key
   */
  public void clear(final String key)
  {
//    final SharedPreferences.Editor editor = prefs.edit();
//    editor.remove(key);
//    editor.commit();
    globalScope.remove(key);
    saveState();
  }

  /**
   * Fetch an item from activity scope
   * @param activity the activity
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
   * Set an item in activity scope
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
    if (activityPersists.containsKey(activity.getLocalClassName()))
    {
      saveState();
//      try
//      {
//        WealdMapper.getServerMapper().writeValueAsString(value);
//      }
//      catch (JsonProcessingException e)
//      {
//        LOG.error("Failed to write value {}: ", value, e);
//        throw new IllegalArgumentException("Failed to set value");
//      }
    }
    scope.put(key, value);
  }

  /**
   * Clear an item in activity scope
   * @param activity the activity
   * @param key the key
   */
  public void clear(final Activity activity, final String key)
  {
    Map<String, Object> scope = activityScope.get(activity.getLocalClassName());
    if (scope != null)
    {
      scope.remove(key);
      if (activityPersists.containsKey(activity.getLocalClassName()))
      {
        saveState();
      }
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
   * Set an item in component scope
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

  /**
   * Clear an item in component scope
   * @param activity the activity
   * @param component the component
   * @param key the key
   */
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

  /**
   * Mark an activity-level item to be persisted
   */
  public void persist(final Activity activity, final String key)
  {
    if (!activityPersists.containsKey(activity.getLocalClassName()) || activityPersists.containsEntry(activity.getLocalClassName(), key))
    {
      activityPersists.put(activity.getLocalClassName(), key);
      if (get(activity, key) != null)
      {
        // Re-set it to force save
        set(activity, key, get(activity, key));
      }
    }
  }

  public void unpersist(final Activity activity, final String key)
  {
    if (activityPersists.containsKey(activity.getLocalClassName()) && activityPersists.containsEntry(activity.getLocalClassName(), key))
    {
      Object value = get(activity, key);
      if (value != null)
      {
        clear(activity, key);
      }
      activityPersists.remove(activity.getLocalClassName(), key);
      if (value != null)
      {
        // Re-set it as we just removed it from persistent and local storage
        set(activity, key, get(activity, key));
      }
    }
  }

  private void saveState()
  {
    final SharedPreferences.Editor editor = prefs.edit();
    try
    {
      editor.putString("fabric", WealdMapper.getMapper().writeValueAsString(this));
    }
    catch (final JsonProcessingException e)
    {
      LOG.error("Failed to save state: ", e);
      throw new IllegalArgumentException("Failed to save state");
    }
    editor.commit();
  }
}
