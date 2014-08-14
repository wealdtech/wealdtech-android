package com.wealdtech.android.fabric;

/**
 */
public interface FabricDataListener<T>
{
  void onDataChanged(T oldData, T newData);
}
