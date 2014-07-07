package com.wealdtech.android.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.wealdtech.android.test.tiles.ClockTile;
import com.wealdtech.android.tiles.TileLayout;
import com.wealdtech.android.utils.ViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class PeriodicUpdateTestActivity extends Activity
{
  private static final Logger LOG = LoggerFactory.getLogger(PeriodicUpdateTestActivity.class);

  public PeriodicUpdateTestActivity()
  {
    BasicLogcatConfigurator.configureDefaultContext();
  }

  public class ViewHolder
  {
    public TileLayout layout;
    public ClockTile clockTile1;
    public ClockTile clockTile2;
  }
  public ViewHolder holder = new ViewHolder();

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    holder.layout = new TileLayout(this);
    holder.layout.setId(ViewUtils.generateViewId());
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    holder.layout.setLayoutParams(layoutParams);
    setContentView(holder.layout);

    holder.clockTile1 = new ClockTile(this);
    holder.clockTile1.setId(ViewUtils.generateViewId());
    holder.layout.addView(holder.clockTile1);

    holder.clockTile2 = new ClockTile(this);
    holder.clockTile2.setId(ViewUtils.generateViewId());
    holder.layout.addView(holder.clockTile2);
  }
}
