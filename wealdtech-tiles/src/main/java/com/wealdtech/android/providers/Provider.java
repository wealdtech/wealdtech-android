package com.wealdtech.android.providers;

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
   * Remove all listeners for this provider.
   */
  void removeDataChangedListeners();

  /**
   * Start providing data
   * @throws IllegalStateException if the provider is not configured to provide data.  Current state can be obtained using
   * {@code getState()}
   */
  void startProviding() throws IllegalStateException;

  /**
   * Stop providing data.
   */
  void stopProviding();

  /**
   * Set the configuration state of the provider.  If we are already providing then this will refetch data or restart polling to
   * ensure that the data remains correct
   *
   * @param configurationState a valid configuration state.
   *
   * @throws java.lang.IllegalStateException if the provider sets {@code CONFIGURED} but cannot provide data
   */
  void setConfigurationState(ConfigurationState configurationState) throws IllegalStateException;

  /**
   * @return {@code true} if this provider is able to return data at current.  This is a check that any parameters or other
   * prerequisites have been set to avoid running useless polling threads; it does not guarantee that any data returned from the
   * provider will not be {@code null}
   */
  boolean canProvideData();
}
