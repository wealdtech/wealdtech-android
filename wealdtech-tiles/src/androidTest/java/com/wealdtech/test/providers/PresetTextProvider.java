package com.wealdtech.test.providers;

import com.wealdtech.android.providers.AbstractProvider;

/**
 * Provide preset text
 */
public class PresetTextProvider extends AbstractProvider<String>
{
  private final String text;

  public PresetTextProvider(final String text)
  {
    super(0);
    this.text = text;
  }

  public String obtainData()
  {
    return text;
  }
}
