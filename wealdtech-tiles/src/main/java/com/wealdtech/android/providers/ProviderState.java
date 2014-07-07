package com.wealdtech.android.providers;

/**
 */
public enum ProviderState
{
  /**
   * The provider is not providing data
   */
  NOT_PROVIDING,
  /**
   * The provider wants to provide data but is not configured
   */
  AWAITING_CONFIGURATION,
  /**
   * The provider is providing data
   */
  PROVIDING
}
