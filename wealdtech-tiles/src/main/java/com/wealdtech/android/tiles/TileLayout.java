package com.wealdtech.android.tiles;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 */
public class TileLayout extends GridLayout
{
  private static final Logger LOG = LoggerFactory.getLogger(TileLayout.class);

  private transient int screenWidth;
  private boolean[][] available;

  private int rows = 5;
  private int cols = 4;
  private int margins = 3;

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
    available = new boolean[cols][rows];
    for (int col = 0; col < cols; col++)
    {
      for (int row = 0; row < rows; row++)
      {
        available[col][row] = true;
      }
    }

    final WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

    final Point size = new Point();
    wm.getDefaultDisplay().getSize(size);

    screenWidth = size.x < size.y - 120 ? size.x : size.y - 120;
  }

  private GridLayout.LayoutParams getSpec(final Tile tile, ViewGroup.LayoutParams specIn)
  {
    final int width = tile.getTileWidth();
    final int height = tile.getTileHeight();

    LOG.debug("Attempting to find space for tile of size ({},{}) in layout of size ({},{})", width, height, cols, rows);
    synchronized (available)
    {
      for (int row = 0; row < rows; row++)
      {
        for (int col = 0; col < cols; col++)
        {
          LOG.debug("Checking location ({},{})", col, row);
          if (available[col][row])
          {
            boolean spaceBigEnough = col + width <= cols && row + height <= rows;
            for (int emptyRow = row; (emptyRow < row + height) && (emptyRow < rows) && spaceBigEnough; emptyRow++)
            {
              for (int emptyCol = col; (emptyCol < col + width) && (emptyCol < cols) && spaceBigEnough; emptyCol++)
              {
                if (!available[emptyCol][emptyRow])
                {
                  spaceBigEnough = false;
                }
              }
            }
            if (spaceBigEnough)
            {
              LOG.debug("Found space large enough at ({},{})", col, row);
              for (int emptyRow = row; emptyRow < row + height; emptyRow++)
              {
                for (int emptyCol = col; emptyCol < col + width; emptyCol++)
                {
                  available[emptyCol][emptyRow] = false;
                }
              }
              Spec rowSpec = GridLayout.spec(row, tile.getTileHeight());
              Spec colSpec = GridLayout.spec(col, tile.getTileWidth());
              final GridLayout.LayoutParams spec;
              if (specIn == null)
              {
                spec = new GridLayout.LayoutParams(rowSpec, colSpec);
              }
              else
              {
                spec = new GridLayout.LayoutParams(specIn);
                spec.rowSpec = rowSpec;
                spec.columnSpec = colSpec;
              }
              int tileSize = (screenWidth / cols) - (2 * margins);
              spec.width = tileSize * tile.getTileWidth() + ((tile.getTileWidth() - 1) * 2 * margins);
              spec.height = tileSize * tile.getTileHeight() + ((tile.getTileHeight() - 1) * 2 * margins);

              spec.setMargins(margins, margins, margins, margins);
              return spec;
            }
          }
        }
      }
    }

    // If we get this far we haven't found anywhere to put it
    throw new RuntimeException("Nowhere found to place tile of size (" + width + "," + height + ")");
  }

  @Override
  public void addView(@Nonnull final View child)
  {
    addView(child, -1, null);
  }

  @Override
  public void addView(@Nonnull final View child, final int index)
  {
    addView(child, index, null);
  }

  @Override
  public void addView(@Nonnull final View child, final ViewGroup.LayoutParams params)
  {
    addView(child, -1, params);
  }

  @Override
  public void addView(@Nonnull final View child, final int index, final ViewGroup.LayoutParams params)
  {
    if (!(child instanceof Tile))
    {
      throw new RuntimeException("Must add tiles to tile layout");
    }
    final Tile tile = (Tile)child;
    final GridLayout.LayoutParams spec = getSpec(tile, params);
    super.addView(tile, index, spec);
  }
}
