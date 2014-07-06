package com.wealdtech.android.test;

import android.app.Activity;
import android.os.Bundle;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.android.debug.hv.ViewServer;
import com.wealdtech.android.providers.Provider;
import com.wealdtech.android.test.providers.DateProvider;
import com.wealdtech.android.test.providers.PresetTextProvider;
import com.wealdtech.android.test.tiles.ClockTile;
import com.wealdtech.android.test.tiles.MultiTextTile;
import com.wealdtech.android.test.tiles.TextTile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 */
public class TileLayoutTestActivity extends Activity
{
  private static final Logger LOG = LoggerFactory.getLogger(TileLayoutTestActivity.class);

  Provider<Date> clockProvider;
  Provider<String> textProvider;

  public TileLayoutTestActivity()
  {
    clockProvider = new DateProvider();
    textProvider = new PresetTextProvider("Hello, world");
    holder = new ViewHolder();
    BasicLogcatConfigurator.configureDefaultContext();
  }

  private class ViewHolder
  {
    public TextTile textTile1;
    public TextTile textTile2;
    public TextTile textTile3;
    public ClockTile clockTile1;
    public ClockTile clockTile2;
    public ClockTile clockTile3;
    public ClockTile clockTile4;
    public MultiTextTile multiTextTile1;
    public MultiTextTile multiTextTile2;
  }
  private ViewHolder holder;

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ViewServer.get(this).addWindow(this);
    setContentView(R.layout.test_activity);

    //    final TileLayout layout = (TileLayout)findViewById(R.id.test_layout);

    holder.textTile1 = (TextTile)findViewById(R.id.test_layout_text_tile_1);
    textProvider.addDataChangedListener(holder.textTile1);
    holder.textTile2 = (TextTile)findViewById(R.id.test_layout_text_tile_2);
    textProvider.addDataChangedListener(holder.textTile2);
    holder.textTile3 = (TextTile)findViewById(R.id.test_layout_text_tile_3);
    textProvider.addDataChangedListener(holder.textTile3);

    holder.clockTile1 = (ClockTile)findViewById(R.id.test_layout_clock_tile_1);
    clockProvider.addDataChangedListener(holder.clockTile1);
    holder.clockTile2 = (ClockTile)findViewById(R.id.test_layout_clock_tile_2);
    clockProvider.addDataChangedListener(holder.clockTile2);
    holder.clockTile3 = (ClockTile)findViewById(R.id.test_layout_clock_tile_3);
    clockProvider.addDataChangedListener(holder.clockTile3);
    holder.clockTile4 = (ClockTile)findViewById(R.id.test_layout_clock_tile_4);
    clockProvider.addDataChangedListener(holder.clockTile4);

    holder.multiTextTile1 = (MultiTextTile)findViewById(R.id.test_layout_multi_text_tile_1);
    textProvider.addDataChangedListener(holder.multiTextTile1);
    holder.multiTextTile2 = (MultiTextTile)findViewById(R.id.test_layout_multi_text_tile_2);
    textProvider.addDataChangedListener(holder.multiTextTile2);

    clockProvider.startProviding();
    textProvider.startProviding();
  }

  @Override
  public void onResume()
  {
    super.onResume();
    ViewServer.get(this).setFocusedWindow(this);
  }

  @Override
  public void onDestroy()
  {
    clockProvider.stopProviding();
    textProvider.stopProviding();
    textProvider.removeDataChangedListener(holder.textTile1);
    textProvider.removeDataChangedListener(holder.textTile2);
    textProvider.removeDataChangedListener(holder.textTile3);
    clockProvider.removeDataChangedListener(holder.clockTile1);
    clockProvider.removeDataChangedListener(holder.clockTile2);
    clockProvider.removeDataChangedListener(holder.clockTile3);
    clockProvider.removeDataChangedListener(holder.clockTile4);
    textProvider.removeDataChangedListener(holder.multiTextTile1);
    textProvider.removeDataChangedListener(holder.multiTextTile2);
    ViewServer.get(this).removeWindow(this);
    super.onDestroy();
  }
}
