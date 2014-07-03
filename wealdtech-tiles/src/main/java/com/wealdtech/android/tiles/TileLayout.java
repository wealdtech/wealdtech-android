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
  private int columns = 4;
  private float spacing = 3;

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
        columns = a.getInteger(attr, columns);
      }
      else if (attr == R.styleable.TileLayout_spacing)
      {
        spacing = a.getDimension(attr, spacing);
      }
    }
    a.recycle();
  }

  private void initLayout()
  {
    available = new boolean[columns][rows];
    for (int col = 0; col < columns; col++)
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
      final int side = width / columns;
      int children = getChildCount();
      for (int i = 0; i < children; i++)
      {
        final View child = getChildAt(i);
        final Tile.LayoutParams lp = (Tile.LayoutParams) child.getLayoutParams();
        int left = (int) (lp.left * side + spacing / 2);
        int right = (int) ((lp.left + lp.colSpan) * side - spacing / 2);
        int top = (int) (lp.top * side + spacing / 2);
        int bottom = (int) ((lp.top + lp.rowSpan) * side - spacing / 2);
        child.layout(left, top, right, bottom);
      }
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    measureVertical(widthMeasureSpec, heightMeasureSpec);
  }

  private void measureVertical(int widthMeasureSpec, int heightMeasureSpec)
  {
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int width = 0;
    int height = 0;

    if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY)
    {
      width = MeasureSpec.getSize(widthMeasureSpec);
    }
    else
    {
      throw new RuntimeException("widthMeasureSpec must be AT_MOST or " + "EXACTLY not UNSPECIFIED when orientation == VERTICAL");
    }


    View child = null;
    int row = 0;
    int side = width / columns;
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++)
    {
      child = getChildAt(i);

      Tile.LayoutParams lp = (Tile.LayoutParams) child.getLayoutParams();

      if (lp.top + lp.rowSpan > row)
      {
        row = lp.top + lp.rowSpan;
      }

      int childHeight = lp.rowSpan * side;
      int childWidth = lp.colSpan * side;
      int heightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
      int widthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);

      child.measure(widthSpec, heightSpec);
    }
    height = row * side;
    // TODO: Figure out a good way to use the heightMeasureSpec...

    setMeasuredDimension(width, height);
  }

  @Override
  public void addView(@Nonnull final View child, final int index, final ViewGroup.LayoutParams params)
  {
    if (!(child instanceof Tile))
    {
      throw new RuntimeException("Must add tiles to tile layout");
    }
    final Tile tile = (Tile) child;
    final Tile.LayoutParams spec = getChildSpec(tile, params);
    super.addView(tile, index, spec);
    child.setClickable(true);
    if (spec.expandable)
    {
      child.setOnClickListener(new ExpandClickListener());
    }
  }

  /**
   * Get the specification for a child tile
   *
   * @param tile
   * @param spec
   * @return
   */
  private Tile.LayoutParams getChildSpec(final Tile tile, ViewGroup.LayoutParams spec)
  {
    final Tile.LayoutParams params = (Tile.LayoutParams) tile.getLayoutParams();
    // TODO set top/left if they are not present
    return params;
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
    @Override
    public void onClick(final View v)
    {
      final Tile.LayoutParams spec = (Tile.LayoutParams) v.getLayoutParams();

      final OnClickListener contractClickListener = new ContractClickListener(spec.left, spec.top, spec.colSpan, spec.rowSpan);
      spec.top = 0;
      spec.left = 0;
      spec.colSpan = columns;
      spec.rowSpan = rows;
      v.setOnClickListener(contractClickListener);
      v.bringToFront();
      childAltered = true;
      invalidate();
    }
  }

  public class ContractClickListener implements OnClickListener
  {
    int left;
    int top;
    int colSpan;
    int rowSpan;

    public ContractClickListener(final int left, final int top, final int colSpan, final int rowSpan)
    {
      this.left = left;
      this.top = top;
      this.colSpan = colSpan;
      this.rowSpan = rowSpan;
    }

    public void onClick(final View v)
    {
      final Tile.LayoutParams spec = (Tile.LayoutParams) v.getLayoutParams();
      spec.left = left;
      spec.top = top;
      spec.colSpan = colSpan;
      spec.rowSpan = rowSpan;
      v.setOnClickListener(new ExpandClickListener());
      v.bringToFront();
      childAltered = true;
      invalidate();
    }
  }
}
