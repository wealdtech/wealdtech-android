package com.wealdtech.android.tiles;

import com.wealdtech.android.providers.Provider;

/**
 */
public interface DataChangedListener<T>
{
  void onDataChanged(T data);

  void setProvider(Provider<T> provider);
}
