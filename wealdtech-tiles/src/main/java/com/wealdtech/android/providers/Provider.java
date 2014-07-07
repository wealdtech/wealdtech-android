package com.wealdtech.android.providers;

import com.wealdtech.android.tiles.DataChangedListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A provider is a generic way of providing information which could come from either a local or remote source.
 * <p/>
 * A provider provides data in one of two ways.  Either
 *
 */
public interface Provider<T>
{
  /**
   * Obtain the current data.
   * @return The current data; can be {@code null}
   * @throws IllegalStateException if the provider is not configured to provide data or {@code startProviding()} has not been called
   */
  @Nullable
  T getData() throws IllegalStateException;

  /**
   * Add a listener to this provider.  The listener will receive notifications when new data is available from this provider.
   * @param listener the listener to add.
   */
  void addDataChangedListener(@Nonnull DataChangedListener<T> listener);

  /**
   * Remove a listener from this provider.  The listener will no longer receive notifications when new data is available from
   * this provider.
   * @param listener the listener to remove.
   */
  void removeDataChangedListener(@Nonnull DataChangedListener<T> listener);

  /**
   * Start providing data
   * @throws IllegalStateException if the provider is not configured to provide data
   */
  void startProviding() throws IllegalStateException;

  /**
   * Stop providing data.
   */
  void stopProviding();

  /**
   * @return {@code true} if the provider is providing data; otherwise {@code false}
   */
  boolean isProviding();
}
