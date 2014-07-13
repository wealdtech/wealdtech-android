package com.wealdtech.android.providers;

/**
 * A chained provider takes configuration of one type and provides data of another
 */
public abstract class ChainedProvider<T, U> extends AbstractProvider<T> implements DataChangedListener<U>
{
  public ChainedProvider(final String name, final long updateInterval)
  {
    super(name, updateInterval);
  }

  public ChainedProvider(final long updateInterval)
  {
    super(updateInterval);
  }
}
