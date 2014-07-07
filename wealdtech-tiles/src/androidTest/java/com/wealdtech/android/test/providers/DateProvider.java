package com.wealdtech.android.test.providers;

import com.wealdtech.android.providers.AbstractProvider;
import com.wealdtech.android.providers.ConfigurationState;

import java.util.Date;

/**
 * Provide the date, updating every second
 */
public class DateProvider extends AbstractProvider<Date>
{
  public DateProvider()
  {
    super("Date", 1000L);
    setConfigurationState(ConfigurationState.CONFIGURED);
  }

  public Date obtainData()
  {
    return new Date();
  }

  @Override
  public boolean canProvideData()
  {
    return true;
  }
}
