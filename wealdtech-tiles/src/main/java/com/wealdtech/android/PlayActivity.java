package com.wealdtech.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.wealdtech.android.tiles.SimpleTextTile;
import com.wealdtech.android.tiles.Tile;
import com.wealdtech.android.tiles.TileLayout;
import com.wealdtech.android.utils.ViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class PlayActivity extends Activity
{
  private static final Logger LOG = LoggerFactory.getLogger(PlayActivity.class);

  public PlayActivity()
  {
    BasicLogcatConfigurator.configureDefaultContext();
  }

  public class ViewHolder
  {
    public TileLayout layout;
    public SimpleTextTile textTile1;
    public SimpleTextTile textTile2;
    public SimpleTextTile textTile3;
    public SimpleTextTile textTile4;
  }
  public ViewHolder holder = new ViewHolder();

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    holder.layout = new TileLayout(this);
    holder.layout.setRows(5);
    holder.layout.setCols(4);
    holder.layout.setId(ViewUtils.generateViewId());
    final TileLayout.LayoutParams layoutParams = new TileLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    holder.layout.setLayoutParams(layoutParams);
    setContentView(holder.layout);

    holder.textTile1 = new SimpleTextTile(this);
    holder.textTile1.onDataChanged("Tile 1");
    final Tile.LayoutParams textTile1Params = holder.textTile1.getLayoutParams();
    textTile1Params.colSpan = 3;
    textTile1Params.rowSpan = 3;
    holder.textTile1.setLayoutParams(textTile1Params);
    holder.layout.addView(holder.textTile1);

    holder.textTile2 = new SimpleTextTile(this);
    holder.textTile2.onDataChanged("Tile 2");
    final Tile.LayoutParams textTile2Params = holder.textTile2.getLayoutParams();
    textTile2Params.colSpan = 2;
    textTile2Params.rowSpan = 2;
    holder.textTile2.setLayoutParams(textTile2Params);
    holder.layout.addView(holder.textTile2);

    holder.textTile3 = new SimpleTextTile(this);
    holder.textTile3.onDataChanged("Tile 3");
    final Tile.LayoutParams textTile3Params = holder.textTile3.getLayoutParams();
    holder.textTile3.setLayoutParams(textTile3Params);
    holder.layout.addView(holder.textTile3);

    holder.textTile4 = new SimpleTextTile(this);
    holder.textTile4.onDataChanged("Tile 4");
    final Tile.LayoutParams textTile4Params = holder.textTile4.getLayoutParams();
    textTile4Params.top = 4;
    textTile4Params.left = 3;
    holder.textTile4.setLayoutParams(textTile4Params);
    holder.layout.addView(holder.textTile4);

    new AsyncTask<Void, Void, Void>()
    {
      int count = 0;
      @Override
      protected Void doInBackground(final Void... params)
      {
        while (true)
        {
          publishProgress();
          try
          {
            Thread.sleep(1000L);
          }
          catch (final InterruptedException ignored)
          {
          }
        }
      }

      @Override
      protected void onProgressUpdate(final Void... item)
      {
        LOG.info("{}: polling update");
        if (count++ % 2 == 0)
        {
          LOG.error("Setting visibility of first tile to GONE");
          holder.textTile1.setVisibility(View.GONE);
        }
        else
        {
          LOG.error("Setting visibility of first tile to VISIBLE");
          holder.textTile1.setVisibility(View.VISIBLE);
        }
        holder.textTile1.requestLayout();
      }
    }.execute(null, null, null);
  }
}
