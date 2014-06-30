package com.wealdtech.android;

import android.app.Activity;
import android.os.Bundle;
import com.wealdtech.android.widget.tiles.ClockTile;
import com.wealdtech.android.widget.tiles.ClockTileProvider;
import com.wealdtech.android.widget.tiles.TileLayout;
import com.wealdtech.android.widget.tiles.TileProvider;

import java.util.Date;

/**
 */
public class TestActivity extends Activity
{
  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.test_activity);

    final TileLayout layout = (TileLayout)findViewById(R.id.test_layout);

    final TileProvider<Date> clockProvider = new ClockTileProvider();

    final ClockTile clockTile1 = (ClockTile)findViewById(R.id.test_layout_clock_tile_1);
    clockTile1.setProvider(clockProvider);
    clockProvider.addDataChangedListener(clockTile1);
    layout.addTile(clockTile1);

    final ClockTile clockTile2 = (ClockTile)findViewById(R.id.test_layout_clock_tile_2);
    clockTile2.setProvider(clockProvider);
    clockProvider.addDataChangedListener(clockTile2);
    layout.addTile(clockTile2);
  }
}
