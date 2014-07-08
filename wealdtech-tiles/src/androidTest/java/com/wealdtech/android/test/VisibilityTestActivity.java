package com.wealdtech.android.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.wealdtech.android.test.tiles.FlipTile;
import com.wealdtech.android.test.tiles.TextTile;
import com.wealdtech.android.tiles.TileLayout;
import com.wealdtech.android.utils.ViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class VisibilityTestActivity extends Activity
{
  private static final Logger LOG = LoggerFactory.getLogger(VisibilityTestActivity.class);

  public VisibilityTestActivity()
  {
    BasicLogcatConfigurator.configureDefaultContext();
  }

  public class ViewHolder
  {
    public TileLayout layout;
    public TextTile textTile;
    public FlipTile flipTile;
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

    holder.textTile = new TextTile(this);
    holder.textTile.setId(ViewUtils.generateViewId());
    holder.layout.addView(holder.textTile);

    holder.flipTile = new FlipTile(this);
    holder.flipTile.setId(ViewUtils.generateViewId());
    holder.layout.addView(holder.flipTile);
  }
}
