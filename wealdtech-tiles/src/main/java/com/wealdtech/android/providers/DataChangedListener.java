package com.wealdtech.android.providers;

import javax.annotation.Nullable;

/**
 * An interface which should be implemented by any class which wishes to obtain notifications when data from a provider
 * has changed.
 */
public interface DataChangedListener<T>
{
  /**
   * Called when data has been changed; allows for real-time updates from providers
   * @param data the new value of the data
   */
  void onDataChanged(@Nullable T data);
}
