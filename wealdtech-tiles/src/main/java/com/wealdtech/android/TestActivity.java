package com.wealdtech.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import com.wealdtech.android.widget.tiles.ClockTile;
import com.wealdtech.android.widget.tiles.ClockTileProvider;
import com.wealdtech.android.widget.tiles.TileLayout;
import com.wealdtech.android.widget.tiles.TileProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;

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

    // setContentView(R.layout.test_activity);

    // final TileLayout layout = (TileLayout)findViewById(R.id.test_layout);
    final TileLayout layout = new TileLayout(this);
    addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
              ViewGroup.LayoutParams.FILL_PARENT));

    final TileProvider<Date> clockProvider = new ClockTileProvider();

    // final ClockTile clockTile1 = (ClockTile)findViewById(R.id.test_layout_clock_tile_1);
    final ClockTile clockTile1 = new ClockTile(this);
    clockTile1.setProvider(clockProvider);
    clockProvider.addDataChangedListener(clockTile1);
    layout.addTile(clockTile1);

    // final ClockTile clockTile2 = (ClockTile)findViewById(R.id.test_layout_clock_tile_2);
    final ClockTile clockTile2 = new ClockTile(this);
    clockTile2.setProvider(clockProvider);
    clockProvider.addDataChangedListener(clockTile2);
    layout.addTile(clockTile2);
  }
}
