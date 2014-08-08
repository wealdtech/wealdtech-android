package com.wealdtech.android.fabric;

import android.content.Context;
import android.content.SharedPreferences;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.wealdtech.jackson.WealdMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
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

  private static final String GLOBAL_STORE = "__global";

  private Fabric()
  {
    prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
  }

  /**
   * Initialise the fabric.
   * @param context the application-level context for the fabric
   * @param name the name for the fabric.  This can be anything but should be unique, so your package name might be a good choice
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

  public <T> T get(final String key, final Class<T> klazz)
  {
    return get(GLOBAL_STORE, key, klazz);
  }

  public <T> void put(final String key, final T value)
  {
    put(GLOBAL_STORE, key, value);
  }

  public void clear(final String key)
  {
    clear(GLOBAL_STORE, key);
  }
}
