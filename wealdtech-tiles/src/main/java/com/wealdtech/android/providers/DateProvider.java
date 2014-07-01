package com.wealdtech.android.providers;

import java.util.Date;

/**
 * Provide the date, updating every second
 */
public class DateProvider extends AbstractProvider<Date>
{
  public DateProvider()
  {
    super(1000L);
  }

  public Date obtainData()
  {
    return new Date();
  }
}
