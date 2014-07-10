package com.wealdtech.android.tiles;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
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
  private boolean forceSquare = false;
  private float spacing = DEFAULT_SPACING;

  // FIXME remove after testing
  private int stateToSave;

  // The current orientation, and an orientation listener
  private int currentOrientation;

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
    // We implement draw() so...
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
   * Calculate the size of a tile side. Calculation returns the largest tile we can have within the constraints of available space
   * and number of rows/columns
   *
   * @return the size of a tile side
   */
  private float calculateTileUnit(final int width, final int height)
  {
    if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
    {
      return Math.min((width - spacing) / portraitCols - spacing, (height - spacing) / portraitRows - spacing);
    }
    else
    {
      return Math.min((width - spacing) / portraitRows - spacing, (height - spacing) / portraitCols - spacing);
    }
  }

  /**
   * Calculate the width of a tile side. Calculation takes up all available space, so can result in non-square tiles
   *
   * @return the width of a tile side
   */
  private float calculateTileWidth(final int width)
  {
    if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
    {
      return (width - spacing) / portraitCols - spacing;
    }
    else
    {
      return (width - spacing) / portraitRows - spacing;
    }
  }

  /**
   * Calculate the height of a tile side. Calculation takes up all available space, so can result in non-square tiles
   *
   * @return the height of a tile side
   */
  private float calculateTileHeight(final int height)
  {
    if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
    {
      return (height - spacing) / portraitRows - spacing;
    }
    else
    {
      return (height - spacing) / portraitCols - spacing;
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
    // Reset the current orientation
    currentOrientation = getResources().getConfiguration().orientation;

    final int width = r - l;
    final int height = b - t;

    // Tile width and height
    final float tileWidth = forceSquare ? calculateTileUnit(width, height) : calculateTileWidth(width);
    final float tileHeight = forceSquare ? calculateTileUnit(width, height) : calculateTileHeight(height);

    if (expandedTile != null)
    {
      // We have an expanded tile; just display that
      expandedTile.layout(l + (int)spacing, t + (int)spacing, r - (int)spacing, b - (int)spacing);
    }
    else
    {
      // Work out number of rows and columns depending on orientation
      final int rows = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? portraitRows : portraitCols;
      final int cols = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? portraitCols : portraitRows;

      // Set up a boolean array which shows which spaces have been taken
      final boolean[][] spaceTaken = new boolean[cols][rows];

      final int numChildren = getChildCount();

      // Need to take two passes.  First we lay out any children with hardcoded positions
      for (int i = 0; i < numChildren; i++)
      {
        final View child = getChildAt(i);
        if (child instanceof Tile && child.getVisibility() != View.GONE)
        {
          final Tile tile = (Tile)child;
          if (!tile.isFloating())
          {
            final int left = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? tile.getTileLeft() : tile.getTileTop();
            final int top = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? tile.getTileTop() : tile.getTileLeft();
            setSpaceOccupied(spaceTaken, left, top, left + tile.getColSpan(), top + tile.getRowSpan());
            final int leftPx = (int)(left * tileWidth + ((left + 1) * spacing));
            final int rightPx = leftPx + (int)((tile.getColSpan() * tileWidth) + ((tile.getColSpan() - 1) * spacing));
            final int topPx = (int)(top * tileHeight + ((top + 1) * spacing));
            final int bottomPx = topPx + (int)((tile.getRowSpan() * tileHeight) + ((tile.getRowSpan() - 1) * spacing));
            tile.layout(leftPx, topPx, rightPx, bottomPx);
          }
        }
      }

      // Second pass we lay out any floating children
      for (int i = 0; i < numChildren; i++)
      {
        final View child = getChildAt(i);
        if (child instanceof Tile && child.getVisibility() != View.GONE)
        {
          final Tile tile = (Tile)child;
          if (tile.isFloating())
          {
            final int left = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? tile.getTileLeft() : tile.getTileTop();
            final int top = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? tile.getTileTop() : tile.getTileLeft();
            final Point start = findSpaceForTile(spaceTaken, left, top, tile.getColSpan(), tile.getRowSpan());
            if (start != null)
            {
              setSpaceOccupied(spaceTaken, start.x, start.y, start.x + tile.getColSpan(), start.y + tile.getRowSpan());
              final int leftPx = (int)(start.x * tileWidth + ((start.x + 1) * spacing));
              final int rightPx = leftPx + (int)((tile.getColSpan() * tileWidth) + ((tile.getColSpan() - 1) * spacing));
              final int topPx = (int)(start.y * tileHeight + ((start.y + 1) * spacing));
              final int bottomPx = topPx + (int)((tile.getRowSpan() * tileHeight) + ((tile.getRowSpan() - 1) * spacing));
              tile.layout(leftPx, topPx, rightPx, bottomPx);
            }
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

    // Work out number of rows and columns depending on orientation
    final int rows = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? portraitRows : portraitCols;
    final int cols = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? portraitCols : portraitRows;

    // Tile width and height
    final float tileWidth = forceSquare ? calculateTileUnit(width, height) : calculateTileWidth(width);
    final float tileHeight = forceSquare ? calculateTileUnit(width, height) : calculateTileHeight(height);

    if (expandedTile != null)
    {
      // We have a single expanded tile; set width and height appropriately
      final int childWidth = (int)((cols * tileWidth) + ((cols - 1) * spacing));
      final int childHeight = (int)((rows * tileHeight) + ((rows - 1) * spacing));
      final int heightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
      final int widthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
      expandedTile.measure(widthSpec, heightSpec);
    }
    else
    {
      final int numChildren = getChildCount();
      for (int i = 0; i < numChildren; i++)
      {
        final Tile tile = (Tile)getChildAt(i);
        if (tile.getVisibility() != View.GONE)
        {
          final int childWidth = (int)((tile.getColSpan() * tileWidth) + ((tile.getColSpan() - 1) * spacing));
          final int childHeight = (int)((tile.getRowSpan() * tileHeight) + ((tile.getRowSpan() - 1) * spacing));
          final int heightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
          final int widthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
          tile.measure(widthSpec, heightSpec);
        }
      }
    }

    width = (int)(((tileWidth + spacing) * cols) + spacing);
    height = (int)(((tileHeight + spacing) * rows) + spacing);
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
    if (tile.hasControls())
    {
      tile.controlLayout.setClickable(true);
      if (expandedTile != null && expandedTile.getId() == tile.getId())
      {
        tile.controlLayout.setOnClickListener(new ContractClickListener(tile));
        tile.onTileExpanded();
      }
      else
      {
        tile.controlLayout.setOnClickListener(new ExpandClickListener(tile));
      }
    }
    super.addView(child, index, params);
    requestLayout();
  }

  public void removeView(@Nonnull final View child)
  {
    super.removeView(child);
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
      tile.onTileExpanded();
      view.setOnClickListener(new ContractClickListener(tile));
      requestLayout();
      invalidate();
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
      tile.onTileContracted();
      view.setOnClickListener(new ExpandClickListener(tile));
      requestLayout();
      invalidate();
    }
  }

  @Override
  public Parcelable onSaveInstanceState()
  {
    final SavedState ss = new SavedState(super.onSaveInstanceState());

    if (expandedTile == null)
    {
      ss.expandedTileId = -1;
    }
    else
    {
      ss.expandedTileId = expandedTile.getId();
    }

    return ss;
  }

  @Override
  public void onRestoreInstanceState(Parcelable state)
  {
    if (!(state instanceof SavedState))
    {
      super.onRestoreInstanceState(state);
      return;
    }

    final SavedState ss = (SavedState)state;
    super.onRestoreInstanceState(ss.getSuperState());

    if (ss.expandedTileId != -1)
    {
      final int numChildren = getChildCount();
      for (int i = 0; i < numChildren; i++)
      {
        final View child = getChildAt(i);
        if (child.getId() == ss.expandedTileId)
        {
          expandedTile = (Tile)child;
        }
      }
    }
  }

  static class SavedState extends BaseSavedState
  {
    int expandedTileId;

    SavedState(final Parcelable superState)
    {
      super(superState);
    }

    private SavedState(final Parcel in)
    {
      super(in);
      this.expandedTileId = in.readInt();
    }

    @Override
    public void writeToParcel(@Nonnull final Parcel out, final int flags)
    {
      super.writeToParcel(out, flags);
      out.writeInt(this.expandedTileId);
    }

    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>()
    {
      public SavedState createFromParcel(final Parcel in)
      {
        return new SavedState(in);
      }

      public SavedState[] newArray(final int size)
      {
        return new SavedState[size];
      }
    };
  }
}
