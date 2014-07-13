package com.wealdtech.android.providers;

import javax.annotation.Nullable;

/**
 * An interface which should be implemented by any class which wishes to obtain notifications when configuration of  a provider has
 * changed.
 */
public interface ConfigurationChangedListener
{
  /**
   * Called when configuration has been changed
   */
  void onConfigurationChanged(@Nullable ConfigurationState state);
}
