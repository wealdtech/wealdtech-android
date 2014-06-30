package com.wealdtech.android.widget.tiles;

/**
 * A tile provider provides data to a tile
 */
public interface TileProvider<T>
{
  T getData();

  void addDataChangedListener(DataChangedListener listener);
}
