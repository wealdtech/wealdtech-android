package com.wealdtech.android.test.providers;

import com.wealdtech.android.providers.AbstractProvider;
import com.wealdtech.android.providers.ConfigurationState;

/**
 * Provide preset text
 */
public class PresetTextProvider extends AbstractProvider<String>
{
  private final String text;

  public PresetTextProvider(final String text)
  {
    super("Preset text", 0);
    this.text = text;
    setConfigurationState(ConfigurationState.CONFIGURED);
  }

  @Override
  public String obtainData()
  {
    return text;
  }

  @Override
  public boolean canProvideData()
  {
    return true;
  }
}
