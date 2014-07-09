package com.wealdtech.android.tiles;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.wealdtech.android.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * A layout based on the idea of individual tiles
 */
public class TileLayout extends ViewGroup
{
  private static final Logger LOG = LoggerFactory.getLogger(TileLayout.class);

  private static final int DEFAULT_PORTRAIT_ROWS = 5;
  private static final int DEFAULT_PORTRAIT_COLS = 4;
  private static final int DEFAULT_SPACING = 3;

  private final List<Tile> tiles;
  private Tile expandedTile;

  // These are the rows and columns we expect to have in portrait mode
  private int portraitCols = DEFAULT_PORTRAIT_COLS;
  private int portraitRows = DEFAULT_PORTRAIT_ROWS;
  private float spacing = DEFAULT_SPACING;

  public TileLayout(final Context context)
  {
    this(context, null);
  }

  public TileLayout(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public TileLayout(final Context context, final AttributeSet attrs, final int defStyle)
  {
    super(context, attrs, defStyle);
    // We implement onDraw() so...
    setWillNotDraw(false);
    setAttrs(attrs, defStyle);
    tiles = new ArrayList<>();
  }

  private void setAttrs(final AttributeSet attrs, final int defStyle)
  {
    final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TileLayout);
    final int N = a.getIndexCount();
    for (int i = 0; i < N; ++i)
    {
      int attr = a.getIndex(i);
      if (attr == R.styleable.TileLayout_rows)
      {
        portraitRows = a.getInteger(attr, portraitRows);
      }
      else if (attr == R.styleable.TileLayout_columns)
      {
        portraitCols = a.getInteger(attr, portraitCols);
      }
      else if (attr == R.styleable.TileLayout_spacing)
      {
        spacing = (int)a.getDimension(attr, spacing);
      }
    }
    a.recycle();
  }

  /**
   * Calculate the size of a tile side.
   *
   * @return the size of a tile side
   */
  private float calculateTileUnit(final int width, final int height)
  {
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
    {
      return (width - spacing) / portraitCols - spacing;
    }
    else
    {
      return (height - spacing) / portraitRows - spacing;
    }
  }

  public void setCols(final int cols)
  {
    this.portraitCols = cols;
  }

  public void setRows(final int rows)
  {
    this.portraitRows = rows;
  }

  @Override
  public void draw(@Nonnull final Canvas canvas)
  {
    if (expandedTile != null)
    {
      // We have an expanded tile; just draw that
      expandedTile.draw(canvas);
    }
    else
    {
      // Standard draw of all children
      super.draw(canvas);
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b)
  {
    final int width = r - l;
    final int height = b - t;

    // Our number of columns and rows depends on our orientation
    final float tileUnit = calculateTileUnit(width, height);

    if (expandedTile != null)
    {
      // We have an expanded tile; just display that
      LOG.error("Have an expanded tile; laying it out only");
      expandedTile.layout(l + (int)spacing, t + (int)spacing, r - (int)spacing, b - (int)spacing);
    }
    else
    {
      final int rows;
      final int cols;
      if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
      {
        rows = portraitRows;
        cols = portraitCols;
      }
      else
      {
        rows = portraitCols;
        cols = portraitRows;
      }

      // Set up a boolean which shows which spaces have been taken
      final boolean[][] spaceTaken = new boolean[cols][rows];

      final int numChildren = getChildCount();
      for (int i = 0; i < numChildren; i++)
      {
        final View child = getChildAt(i);
        if (child instanceof Tile && child.getVisibility() != View.GONE)
        {
          final Tile tile = (Tile)child;
          final Tile.LayoutParams lp = tile.getLayoutParams();
          final Point start = findSpaceForTile(spaceTaken, lp.left, lp.top, lp.colSpan, lp.rowSpan);
          if (start != null)
          {
            setSpaceOccupied(spaceTaken, start.x, start.y, start.x + lp.colSpan, start.y + lp.rowSpan);
            final int left = (int)(start.x * tileUnit + ((start.x + 1) * spacing));
            final int right = left + (int)((lp.colSpan * tileUnit) + ((lp.colSpan - 1) * spacing));
            final int top = (int)(start.y * tileUnit + ((start.y + 1) * spacing));
            final int bottom = top + (int)((lp.rowSpan * tileUnit) + ((lp.rowSpan - 1) * spacing));
            tile.layout(left, top, right, bottom);
          }
        }
      }
    }
  }

  /**
   * Find space in the grid for a tile
   */
  private static Point findSpaceForTile(final boolean[][] spaceTaken,
                                        final int left,
                                        final int top,
                                        final int width,
                                        final int height)
  {
    if (left != -1 && top != -1)
    {
      return new Point(left, top);
    }
    final int cols = spaceTaken.length;
    final int rows = spaceTaken[0].length;
    // Find all empty spaces which could fit our tile and see if our tile fits
    for (int row = 0; row <= rows - width; row++)
    {
      for (int col = 0; col <= cols - height; col++)
      {
        if (!spaceTaken[col][row])
        {
          final int right = col + width;
          final int bottom = row + height;
          if (isSpaceBigEnough(spaceTaken, row, col, right, bottom))
          {
            return new Point(col, row);
          }
        }
      }
    }
    return null;
  }

  private static boolean isSpaceBigEnough(final boolean[][] spaceTaken,
                                          final int left,
                                          final int top,
                                          final int right,
                                          final int bottom)
  {
    final int cols = spaceTaken.length;
    final int rows = spaceTaken[0].length;
    if (right > cols || bottom > rows)
    {
      return false;
    }
    boolean spaceBigEnough = true;
    for (int emptyRow = top; emptyRow < bottom && emptyRow < rows && spaceBigEnough; emptyRow++)
    {
      for (int emptyCol = left; emptyCol < right && emptyCol < cols && spaceBigEnough; emptyCol++)
      {
        spaceBigEnough = !spaceTaken[emptyCol][emptyRow];
      }
    }
    return spaceBigEnough;
  }

  /**
   * Set space as occupied in the space taken array
   */
  private static void setSpaceOccupied(final boolean[][] spaceTaken,
                                       final int left,
                                       final int top,
                                       final int right,
                                       final int bottom)
  {
    for (int row = top; row < bottom; row++)
    {
      for (int col = left; col < right; col++)
      {
        spaceTaken[col][row] = true;
      }
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    // FIXME sort out orientation for non-square layouts
    final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int width;
    int height;

    if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY)
    {
      width = MeasureSpec.getSize(widthMeasureSpec);
    }
    else
    {
      throw new RuntimeException("widthMeasureSpec must be AT_MOST or EXACTLY not UNSPECIFIED when orientation == VERTICAL");
    }
    if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY)
    {
      height = MeasureSpec.getSize(heightMeasureSpec);
    }
    else
    {
      throw new RuntimeException("heightMeasureSpec must be AT_MOST or EXACTLY not UNSPECIFIED when orientation == VERTICAL");
    }

    final int rows;
    final int cols;
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
    {
      rows = portraitRows;
      cols = portraitCols;
    }
    else
    {
      rows = portraitCols;
      cols = portraitRows;
    }

    // Tile unit is the size of the side of a tile
    final float tileUnit = calculateTileUnit(width, height);
    //    final float tileUnit = (width < height ? width - spacing : height - spacing) / cols - spacing;

    final int numChildren = getChildCount();
    for (int i = 0; i < numChildren; i++)
    {
      // We only lay out tiles here.  Other items are part of the overlay and calculated in-loop
      final View child = getChildAt(i);
      if (child instanceof Tile && child.getVisibility() != View.GONE)
      {
        final Tile tile = (Tile)child;
        Tile.LayoutParams lp = tile.getLayoutParams();

        final int childWidth = (int)((lp.colSpan * tileUnit) + ((lp.colSpan - 1) * spacing));
        final int childHeight = (int)((lp.rowSpan * tileUnit) + ((lp.rowSpan - 1) * spacing));
        int heightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
        int widthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);

        tile.measure(widthSpec, heightSpec);
      }
    }

    width = (int)(((tileUnit + spacing) * cols) + spacing);
    height = (int)(((tileUnit + spacing) * rows) + spacing);
    setMeasuredDimension(width, height);
  }

  @Override
  public void addView(@Nonnull final View child, final int index, final ViewGroup.LayoutParams params)
  {
    if (!(child instanceof Tile))
    {
      throw new RuntimeException("Can only add tiles to tile layout");
    }
    final Tile tile = (Tile)child;
    tiles.add(tile);
    super.addView(tile, index, params);
    if (tile.hasControls())
    {
      tile.controlLayout.setClickable(true);
      tile.controlLayout.setOnClickListener(new ExpandClickListener(tile));
    }
    requestLayout();
  }

  public void removeView(@Nonnull final View child)
  {
    if (!(child instanceof Tile))
    {
      throw new RuntimeException("Can only remove tiles from tile layout");
    }
    tiles.remove(child);
    requestLayout();
  }

  public class ExpandClickListener implements OnClickListener
  {
    private final Tile tile;

    public ExpandClickListener(final Tile tile)
    {
      this.tile = tile;
    }

    @Override
    public void onClick(final View view)
    {
      expandedTile = tile;
      final OnClickListener contractClickListener = new ContractClickListener(tile);
      view.setOnClickListener(contractClickListener);
      requestLayout();
    }
  }

  public class ContractClickListener implements OnClickListener
  {
    private final Tile tile;

    public ContractClickListener(final Tile tile)
    {
      this.tile = tile;
    }

    public void onClick(final View view)
    {
      expandedTile = null;
      view.setOnClickListener(new ExpandClickListener(tile));
      requestLayout();
    }
  }
}
