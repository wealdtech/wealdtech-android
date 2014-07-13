package com.wealdtech.android.providers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A provider is a generic way of providing information which could come from either a local or remote source.
 * <p/>
 * A provider provides data in one of two ways.
 *
 */
public interface Provider<T>
{
  /**
   * Obtain the data.  This is different from {@code getData()} in that {@code getData()} never invokes the provider but just
   * returns the current data, whereas this method re-fetches the information from wherever the provider obtains it.
   * In general this method should not be called by systems outside of the provider's control as it could cause excessive network
   * or processing load.
   * @return  Newly-obtained data; can be {@code null}
   */
  @Nullable
  T obtainData();

  /**
   * Get the current data.
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

  /**
   * @return a string representing the current configuration of the provider.  Can be {@code null} if the provider does not have
   * any configuration.
   */
  @Nullable String getConfiguration();

  /**
   * @return the name of the provider
   */
  String getName();

  /**
   * @return the number of seconds between obtaining data; can be 0 if this is not a polling provider
   */
  long getUpdateInterval();


  /**
   * Add a listener to this provider.  The listener will receive notifications when new data is available from this provider.
   * @param listener the listener to add.
   */
  void addConfigurationChangedListener(@Nonnull ConfigurationChangedListener listener);

  /**
   * Remove a listener from this provider.  The listener will no longer receive notifications when new data is available from
   * this provider.
   * @param listener the listener to remove.
   */
  void removeConfigurationChangedListener(@Nonnull ConfigurationChangedListener listener);

  void removeConfigurationChangedListeners();
}

