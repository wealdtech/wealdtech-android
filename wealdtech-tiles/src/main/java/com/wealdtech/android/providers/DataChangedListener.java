package com.wealdtech.android.providers;

/**
 * An interface which should be implemented by any class which wishes to obtain notifications when data from a provider
 * has changed.
 */
public interface DataChangedListener<T>
{
  void onDataChanged(T data);
}
