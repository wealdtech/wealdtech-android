package com.wealdtech.android;

import android.app.Activity;
import android.os.Bundle;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.wealdtech.android.providers.DateProvider;
import com.wealdtech.android.providers.PresetTextProvider;
import com.wealdtech.android.providers.Provider;
import com.wealdtech.android.tiles.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 */
public class TestActivity extends Activity
{
  private static final Logger LOG = LoggerFactory.getLogger(TestActivity.class);

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    BasicLogcatConfigurator.configureDefaultContext();

    LOG.error("Starting test activity");

    setContentView(R.layout.test_activity);

    LOG.error("Finished setting content view");
    final TileLayout layout = (TileLayout)findViewById(R.id.test_layout);

    final Provider<Date> clockProvider = new DateProvider();
    final Provider<String> textProvider = new PresetTextProvider("Hello, world");

    final TextTile textTile1 = (TextTile)findViewById(R.id.test_layout_text_tile_1);
    textProvider.addDataChangedListener(textTile1);

    final ClockTile clockTile1 = (ClockTile)findViewById(R.id.test_layout_clock_tile_1);
    clockProvider.addDataChangedListener(clockTile1);

    final ClockTile clockTile2 = (ClockTile)findViewById(R.id.test_layout_clock_tile_2);
    clockProvider.addDataChangedListener(clockTile2);

    final ClockTile clockTile3 = (ClockTile)findViewById(R.id.test_layout_clock_tile_3);
    clockProvider.addDataChangedListener(clockTile3);

    final ClockTile clockTile4 = (ClockTile)findViewById(R.id.test_layout_clock_tile_4);
    clockProvider.addDataChangedListener(clockTile4);

    clockProvider.startProviding();
    textProvider.startProviding();
  }
}
