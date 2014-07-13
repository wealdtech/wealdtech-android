package com.wealdtech.android.test.providers;

import com.wealdtech.android.providers.AbstractProvider;
import com.wealdtech.android.providers.ConfigurationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Provide the date, updating every second
 */
public class DateProvider extends AbstractProvider<Date>
{
  private static final Logger LOG = LoggerFactory.getLogger(DateProvider.class);

  private static final Long DEFAULT_UPDATE_INTERVAL = 1000L;
  public DateProvider()
  {
    this(DEFAULT_UPDATE_INTERVAL);
  }

  public DateProvider(final long updateInterval)
  {
    super("Date", updateInterval);
    setConfigurationState(ConfigurationState.CONFIGURED);
  }

  public Date obtainData()
  {
    return new Date();
  }

  @Override
  public String getConfiguration()
  {
    // We have no configuration
    return null;
  }

  @Override
  public boolean canProvideData()
  {
    return true;
  }
}
