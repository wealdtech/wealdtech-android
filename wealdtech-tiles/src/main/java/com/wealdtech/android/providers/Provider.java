package com.wealdtech.android.providers;

import com.wealdtech.android.tiles.DataChangedListener;

/**
 * A tile provider provides data to a tile
 */
public interface Provider<T>
{
  T getData();

  void addDataChangedListener(DataChangedListener listener);
}
