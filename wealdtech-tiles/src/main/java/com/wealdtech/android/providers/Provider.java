package com.wealdtech.android.providers;

import com.wealdtech.android.tiles.DataChangedListener;

/**
 * A tile provider provides data to a tile
 */
public interface Provider<T>
{
  /**
   * Obtain the current data
   */
  T getData();

  /**
   * Add a listener
   * @param listener
   */
  void addDataChangedListener(DataChangedListener<T> listener);

  void startProviding();

  void stopProviding();

  boolean isProviding();
}
