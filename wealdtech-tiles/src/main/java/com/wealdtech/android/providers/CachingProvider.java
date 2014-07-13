package com.wealdtech.android.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A caching provider extends the abstract provider with the ability to cache the results for a given amount of time.
 * The data cached is persistent, so can be shared between different instances of the provider.  It is, however, held in memory
 * so there's no guarantee as to how long it will stay around.
 *
 * The underlying provider is able to provide but does not do so on a polling basis.  Instead the caching provider obtains the
 * data from the underlying provider on either an ad-hoc or polled basis, and in addition uses a static cache to obtain data if it is
 * requested inbetween times
 */
public class CachingProvider<T> extends AbstractProvider<T> implements ConfigurationChangedListener
{
  private static final Logger LOG = LoggerFactory.getLogger(CachingProvider.class);

  private static final Map<String, CachedResult> cache = new ConcurrentHashMap<>();

  private Provider<T> underlying;

  public CachingProvider(final Provider<T> underlying, final long lifetime)
  {
    super("Caching " + underlying.getName(), lifetime);
    this.underlying = underlying;
    setConfigurationState(canProvideData() ? ConfigurationState.CONFIGURED : ConfigurationState.NOT_CONFIGURED);
  }

  @Override
  public T obtainData()
  {
    final CachedResult<T> cachedResult = obtainCachedResult();
    if (cachedResult != null && cachedResult.expiry.after(new Date()))
    {
      // We have a valid cached item; use it
      return cachedResult.result;
    }
    else
    {
      // We have no item, or an expired item.  Fetch a new one
      final T result = underlying.obtainData();
      final CachedResult<T> newResult = new CachedResult<>(result, new Date(new Date().getTime() + getUpdateInterval()));
      storeCachedResult(newResult);
      return result;
    }
  }

  @Override
  public void startProviding()
  {
    super.startProviding();
    underlying.startProviding();
  }

  @Override
  public void stopProviding()
  {
    underlying.stopProviding();
    super.stopProviding();
  }

  @Nullable
  @Override
  public String getConfiguration()
  {
    return "Caching " + underlying.getConfiguration();
  }

  @SuppressWarnings("unchecked")
  public CachedResult<T> obtainCachedResult()
  {
    // See if we already have something
    final String key = cacheHash(getConfiguration());
    return (CachedResult<T>)cache.get(key);
  }

  public void storeCachedResult(final CachedResult<T> cachedResult)
  {
    final String key = cacheHash(getConfiguration());
    cache.put(key, cachedResult);
  }

  /**
   * Generate a cache key from a configuration string
   * @param string the string to hash
   * @return the hash
   */
  private static String cacheHash(final String string)
  {
    long h = 1125899906842597L; // prime
    int len = string.length();

    for (int i = 0; i < len; i++)
    {
      h = 31 * h + string.charAt(i);
    }
    return Long.toHexString(h);
  }

  public static class CachedResult<T>
  {
    private Date expiry;
    private T result;

    public CachedResult(final T result, final Date expiry)
    {
      this.expiry = expiry;
      this.result = result;
    }
  }

  @Override
  public boolean canProvideData()
  {
    return underlying != null && underlying.canProvideData();
  }

  @Override
  public void onConfigurationChanged(final ConfigurationState state)
  {
    setConfigurationState(canProvideData() ? ConfigurationState.CONFIGURED : ConfigurationState.NOT_CONFIGURED);
  }

  // TODO write a cache cleaner asynctask
}
