package com.wealdtech.android.widget.tiles;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class TileLayout extends GridLayout
{
  private static final Logger LOG = LoggerFactory.getLogger(TileLayout.class);

  private transient int screenWidth;

  private List<Integer> fullTo = new ArrayList<>();
  private int rows = 4;
  private int cols = 4;

  public TileLayout(final Context context)
  {
    this(context, null, 0);
  }

  public TileLayout(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public TileLayout(final Context context, final AttributeSet attrs, final int defStyle)
  {
    super(context, attrs, defStyle);
    initLayout(context);
  }

  private void initLayout(final Context context)
  {
    setColumnCount(cols);
    setRowCount(rows);

    fullTo.clear();
    for (int i = 0; i < rows; i++)
    {
      fullTo.add(i, 0);
    }

    final WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

    final Point size = new Point();
    wm.getDefaultDisplay().getSize(size);

    screenWidth = size.x < size.y ? size.x : size.y;
  }

  public boolean addTile(final Tile tile, final ViewGroup.LayoutParams params)
  {
    LOG.error("addTile()");
    // Find a suitable space
    Spec rowSpec = null;
    Spec colSpec = null;
    for (int row = 0; row < rows; row++)
    {
      // TODO handle tiles with >1 height
      if (cols - fullTo.get(row) >= tile.getWidth())
      {
        rowSpec = GridLayout.spec(row, tile.getTileHeight());
        colSpec = GridLayout.spec(fullTo.get(row), tile.getTileWidth());
        fullTo.set(row, fullTo.get(row) + tile.getTileWidth());
        break;
      }
    }
    if (rowSpec == null || colSpec == null)
    {
      LOG.error("Failed to find space for tile");
      return false;
    }
    GridLayout.LayoutParams spec = new GridLayout.LayoutParams(rowSpec, colSpec);
    spec.width = screenWidth * tile.getTileWidth() / 4;
    spec.height = screenWidth * tile.getTileHeight() / 4;
    addView(tile.view, spec);
    return true;
  }

  @Override
  public void addView(final View child)
  {
    LOG.error("addView(View)");
    if (child instanceof Tile)
    {
      addTile((Tile)child, null);
    }
    else
    {
      super.addView(child);
    }
  }

  @Override
  public void addView(final View child, final int index)
  {
    LOG.error("addView(View, index)");
    if (child instanceof Tile)
    {
      addTile((Tile)child, null);
    }
    else
    {
      super.addView(child, index);
    }
  }

  @Override
  public void addView(final View child, final ViewGroup.LayoutParams params)
  {
    LOG.error("addView(View, LayoutParams)");
    if (child instanceof Tile)
    {
      addTile((Tile)child, params);
    }
    else
    {
      super.addView(child, params);
    }
  }

  @Override
  public void addView(final View child, final int index, final ViewGroup.LayoutParams params)
  {
    LOG.error("addView(View, index, LayoutParams)");
    if (child instanceof Tile)
    {
      addTile((Tile)child, params);
    }
    else
    {
      super.addView(child, index, params);
    }
  }
}
