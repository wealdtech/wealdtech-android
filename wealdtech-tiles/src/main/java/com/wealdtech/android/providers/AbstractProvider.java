package com.wealdtech.android.providers;

import com.wealdtech.android.tiles.DataChangedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A provider provides data.  The data provided can be accessed in one of two ways:
 * <ul>
 *   <li>Directly, by calling {@code getData()}</li>
 *   <li>When the data changes, by adding a {@code DataChangedListener} through {@code addDataChangedListener()}</li>
 * </ul>
 */
public abstract class AbstractProvider<T> implements Provider<T>
{
  private final List<DataChangedListener> listeners = new ArrayList<>();

  private T data;

  /**
   * Obtain the current data
   */
  public T getData()
  {
    return data;
  }

  protected void setData(final T data)
  {
    this.data = data;
  }

  @Override
  public void addDataChangedListener(final DataChangedListener listener)
  {
    listeners.add(listener);
  }

  protected void notifyListeners()
  {
    for (final DataChangedListener listener : listeners)
    {
      listener.onDataChanged();
    }
  }
}
