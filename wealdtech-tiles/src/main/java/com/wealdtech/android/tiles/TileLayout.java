package com.wealdtech.android.tiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.wealdtech.android.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Tile layout
 */
public class TileLayout extends ViewGroup
{
  private static final Logger LOG = LoggerFactory.getLogger(TileLayout.class);

  private boolean[][] available;

  private int rows = 4;
  private int cols = 4;
  private float spacing = 3;

  /** Set to true whenever one of our children has been altered directly by the layout */
  private boolean childAltered = false;

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
    setWillNotDraw(false);
    setAttrs(attrs, defStyle);
    initLayout();
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
        rows = a.getInteger(attr, rows);
      }
      else if (attr == R.styleable.TileLayout_columns)
      {
        cols = a.getInteger(attr, cols);
      }
      else if (attr == R.styleable.TileLayout_spacing)
      {
        spacing = (int)a.getDimension(attr, spacing);
      }
    }
    a.recycle();
  }

  private void initLayout()
  {
    available = new boolean[cols][rows];
    for (int col = 0; col < cols; col++)
    {
      for (int row = 0; row < rows; row++)
      {
        available[col][row] = true;
      }
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b)
  {
    if (changed || childAltered)
    {
      childAltered = false;
      final int width = r - l;
      final int height = b - t;
      final float tileUnit = (int)((width < height ? width - spacing : height - spacing) / cols - spacing);
      for (int i = 0; i < getChildCount(); i++)
      {
        if (getChildAt(i) instanceof Tile)
        {
          final Tile tile = (Tile)getChildAt(i);
          final Tile.LayoutParams lp = tile.getLayoutParams();
          final int left = (int)(lp.left * tileUnit + ((lp.left + 1) * spacing));
          final int right = (int)(left + (lp.colSpan * tileUnit) + ((lp.colSpan - 1) * spacing));
          final int top = (int)(lp.top * tileUnit + ((lp.top + 1) * spacing));
          final int bottom = top + (int)((lp.rowSpan * tileUnit) + ((lp.rowSpan - 1) * spacing));
          tile.layout(left, top, right, bottom);
        }
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


    // Tile unit is the size of the side of a tile
    final float tileUnit = (width < height ? width - spacing : height - spacing) / cols - spacing;
    for (int i = 0; i < getChildCount(); i++)
    {
      // We only lay out tiles here.  Other items are part of the overlay and calculated in-loop
      if (getChildAt(i) instanceof Tile)
      {
        final Tile tile = (Tile)getChildAt(i);

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
      throw new RuntimeException("Must add tiles to tile layout");
    }
    final Tile tile = (Tile)child;
    final Tile.LayoutParams spec = getChildSpec(tile, params);
    super.addView(tile, index, spec);
    if (tile.hasControls())
    {
      final View controlLayout = tile.controlLayout;
      controlLayout.setClickable(true);
      controlLayout.setOnClickListener(new ExpandClickListener(tile));
    }
  }

  /**
   * Get the specification for a child tile, calculating items such as position if not already specified
   */
  private Tile.LayoutParams getChildSpec(final Tile tile, ViewGroup.LayoutParams spec)
  {
    final Tile.LayoutParams params = tile.getLayoutParams();
    params.incorporateSpec(spec);
    if (params.top == -1 || params.left == -1)
    {
      synchronized (available)
      {
        boolean foundSpace = false;
        for (int row = 0; row < rows && !foundSpace; row++)
        {
          for (int col = 0; col < cols && !foundSpace; col++)
          {
            LOG.debug("Checking location ({},{})", col, row);
            if (available[col][row])
            {
              boolean spaceBigEnough = col + params.colSpan <= cols && row + params.rowSpan <= rows;
              for (int emptyRow = row; (emptyRow < row + params.rowSpan) && (emptyRow < rows) && spaceBigEnough; emptyRow++)
              {
                for (int emptyCol = col; (emptyCol < col + params.colSpan) && (emptyCol < cols) && spaceBigEnough; emptyCol++)
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
                params.top = row;
                params.left = col;
                foundSpace = true;
              }
            }
          }
        }
      }
    }
    if (params.top == -1 || params.left == -1)
    {
      // Still unset - couldn't find anywhere to put it!
      throw new RuntimeException("Nowhere found to place tile of size (" + params.colSpan + "," + params.rowSpan + ")");
    }

    updateLayoutOccupancy(params);
    return params;
  }

  private void updateLayoutOccupancy(final Tile.LayoutParams params)
  {
    for (int emptyRow = params.top; emptyRow < params.top + params.rowSpan; emptyRow++)
    {
      for (int emptyCol = params.left; emptyCol < params.left + params.colSpan; emptyCol++)
      {
        available[emptyCol][emptyRow] = false;
      }
    }
  }

  //  private GridLayout.LayoutParams getSpec(final Tile tile, ViewGroup.LayoutParams specIn)
  //  {
  //    final int width = tile.getTileWidth();
  //    final int height = tile.getTileHeight();
  //
  //    LOG.debug("Attempting to find space for tile of size ({},{}) in layout of size ({},{})", width, height, cols, rows);
  //    synchronized (available)
  //    {
  //      for (int row = 0; row < rows; row++)
  //      {
  //        for (int col = 0; col < cols; col++)
  //        {
  //          LOG.debug("Checking location ({},{})", col, row);
  //          if (available[col][row])
  //          {
  //            boolean spaceBigEnough = col + width <= cols && row + height <= rows;
  //            for (int emptyRow = row; (emptyRow < row + height) && (emptyRow < rows) && spaceBigEnough; emptyRow++)
  //            {
  //              for (int emptyCol = col; (emptyCol < col + width) && (emptyCol < cols) && spaceBigEnough; emptyCol++)
  //              {
  //                if (!available[emptyCol][emptyRow])
  //                {
  //                  spaceBigEnough = false;
  //                }
  //              }
  //            }
  //            if (spaceBigEnough)
  //            {
  //              LOG.debug("Found space large enough at ({},{})", col, row);
  //              for (int emptyRow = row; emptyRow < row + height; emptyRow++)
  //              {
  //                for (int emptyCol = col; emptyCol < col + width; emptyCol++)
  //                {
  //                  available[emptyCol][emptyRow] = false;
  //                }
  //              }
  //              Spec rowSpec = GridLayout.spec(row, tile.getTileHeight());
  //              Spec colSpec = GridLayout.spec(col, tile.getTileWidth());
  //              final GridLayout.LayoutParams spec;
  //              if (specIn == null)
  //              {
  //                spec = new GridLayout.LayoutParams(rowSpec, colSpec);
  //              }
  //              else
  //              {
  //                spec = new GridLayout.LayoutParams(specIn);
  //                spec.rowSpec = rowSpec;
  //                spec.columnSpec = colSpec;
  //              }
  //              int tileSize = (screenWidth / cols) - (2 * spacing);
  //              spec.width = tileSize * tile.getTileWidth() + ((tile.getTileWidth() - 1) * 2 * spacing);
  //              spec.height = tileSize * tile.getTileHeight() + ((tile.getTileHeight() - 1) * 2 * spacing);
  //
  //              spec.setMargins(spacing, spacing, spacing, spacing);
  //              return spec;
  //            }
  //          }
  //        }
  //      }
  //    }
  //
  //    // If we get this far we haven't found anywhere to put it
  //    throw new RuntimeException("Nowhere found to place tile of size (" + width + "," + height + ")");
  //  }
  //
  //  @Override
  //  public void addView(@Nonnull final View child)
  //  {
  //    addView(child, -1, null);
  //  }
  //
  //  @Override
  //  public void addView(@Nonnull final View child, final int index)
  //  {
  //    addView(child, index, null);
  //  }
  //
  //  @Override
  //  public void addView(@Nonnull final View child, final ViewGroup.LayoutParams params)
  //  {
  //    addView(child, -1, params);
  //  }
  //
  //  @Override
  //  public void addView(@Nonnull final View child, final int index, final ViewGroup.LayoutParams params)
  //  {
  //    if (!(child instanceof Tile))
  //    {
  //      throw new RuntimeException("Must add tiles to tile layout");
  //    }
  //    final Tile tile = (Tile)child;
  //    final GridLayout.LayoutParams spec = getSpec(tile, params);
  //    super.addView(tile, index, spec);
  //
  //    child.setClickable(true);
  //    child.setOnClickListener(new ExpandClickListener());
  //  }
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
      final Tile.LayoutParams spec = tile.getLayoutParams();

      final OnClickListener contractClickListener = new ContractClickListener(tile, spec.left, spec.top, spec.colSpan,
                                                                              spec.rowSpan);
      spec.top = 0;
      spec.left = 0;
      spec.colSpan = cols;
      spec.rowSpan = rows;
      view.setOnClickListener(contractClickListener);
      tile.onTileExpanded();
      tile.bringToFront();
      childAltered = true;
      invalidate();
    }
  }

  public class ContractClickListener implements OnClickListener
  {
    private final Tile tile;
    int left;
    int top;
    int colSpan;
    int rowSpan;

    public ContractClickListener(final Tile tile, final int left, final int top, final int colSpan, final int rowSpan)
    {
      this.tile = tile;
      this.left = left;
      this.top = top;
      this.colSpan = colSpan;
      this.rowSpan = rowSpan;
    }

    public void onClick(final View view)
    {
      final Tile.LayoutParams spec = tile.getLayoutParams();
      spec.left = left;
      spec.top = top;
      spec.colSpan = colSpan;
      spec.rowSpan = rowSpan;
      view.setOnClickListener(new ExpandClickListener(this.tile));
      tile.onTileContracted();
      tile.bringToFront();
      childAltered = true;
      invalidate();
    }
  }
}
