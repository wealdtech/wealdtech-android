package com.wealdtech.android.providers;

/**
 * Provide preset text
 */
public class PresetTextProvider extends AbstractProvider<String>
{
  public PresetTextProvider(final String text)
  {
    setData(text);
  }
}
