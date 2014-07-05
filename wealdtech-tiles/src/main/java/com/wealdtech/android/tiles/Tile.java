package com.wealdtech.android.tiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.wealdtech.android.R;
import com.wealdtech.android.providers.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * A Tile is a UI element which is of a fixed ratio of width to height and designed to work within a TileLayout.
 */
public abstract class Tile<T> extends FrameLayout
{
  private static final Logger LOG = LoggerFactory.getLogger(Tile.class);

  /** The provider for the data */
  protected Provider<T> provider;

  /** The controls for the tile */
  protected final View controlLayout;

  /** If the tile is currently expanded */
  private boolean expanded = false;

  public Tile(final Context context)
  {
    this(context, null, 0);
  }

  public Tile(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public Tile(final Context context, final AttributeSet attrs, final int defStyle)
  {
    this(context, attrs, defStyle, Editable.NEVER, false);
  }

  public Tile(final Context context,
              final AttributeSet attrs,
              final int defStyle,
              final Editable editable,
              final boolean expandable)
  {
    super(context, attrs, defStyle);
    setAttrs(context, attrs, defStyle, editable, expandable);
    controlLayout = initControlView(context);
    final LayoutParams params = getLayoutParams();
  }

  private View initControlView(final Context context)
  {
    final TextView controlView;
    if (hasControls())
    {
      controlView = new TextView(context);
      controlView.setText("+");
      controlView.setBackgroundColor(Color.TRANSPARENT);
      controlView.setTextColor(Color.GREEN);
      controlView.setTextSize(24);

      controlView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                               ViewGroup.LayoutParams.WRAP_CONTENT,
                                                               Gravity.BOTTOM | Gravity.RIGHT));
      addView(controlView);
    }
    else
    {
      // No controls
      controlView = null;
    }
    return controlView;
  }

  private void setAttrs(final Context context,
                        final AttributeSet attrs,
                        final int defStyle,
                        final Editable editable,
                        final boolean expandable)
  {
    final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    params.editable = editable;
    params.expandable = expandable;
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Tile);
    final int N = a.getIndexCount();
    for (int i = 0; i < N; ++i)
    {
      int attr = a.getIndex(i);
      // When we have auto-layout then top and left will go away.
      if (attr == R.styleable.Tile_tile_top)
      {
        params.top = a.getInteger(attr, -1);
      }
      else if (attr == R.styleable.Tile_tile_left)
      {
        params.left = a.getInteger(attr, -1);
      }
      else if (attr == R.styleable.Tile_tile_colspan)
      {
        params.colSpan = a.getInteger(attr, params.colSpan);
      }
      else if (attr == R.styleable.Tile_tile_rowspan)
      {
        params.rowSpan = a.getInteger(attr, params.rowSpan);
      }
      else if (attr == R.styleable.Tile_tile_expandable)
      {
        params.expandable = calcExpandable(expandable, a.getBoolean(attr, params.expandable));
      }
      else if (attr == R.styleable.Tile_tile_editable)
      {
        final String editableStr = a.getString(attr);
        if (editableStr != null)
        {
          params.editable = calcEditable(editable, Editable.valueOf(editableStr));
        }
      }
    }
    a.recycle();

    setLayoutParams(params);
    setBackgroundResource(R.drawable.tile_border);
  }

  /**
   * Work out the expandable state of this tile.  The state is the lesser of able, which is the tile's inherent ability, and
   * requested, which is the requested ability
   *
   * @param able the tile's inherent ability
   * @param requested the implementer's requested ability
   * @return the lesser of the inherent and requested abilities
   */
  private boolean calcExpandable(final boolean able, final boolean requested)
  {
    return able && requested;
  }

  /**
   * Work out the editable state of this tile.  The state is the lesser of able, which is the tile's inherent ability, and
   * requested, which is the requested ability
   *
   * @param able the tile's inherent ability
   * @param requested the implementer's requested ability
   * @return the lesser of the inherent and requested abilities
   */
  private Editable calcEditable(final Editable able, final Editable requested)
  {
    final Editable result;
    if (able == Editable.NEVER)
    {
      result = Editable.NEVER;
    }
    else if (able == Editable.ALWAYS)
    {
      result = requested;
    }
    else if (able == Editable.WHEN_CONTRACTED && (requested == Editable.WHEN_CONTRACTED || requested == Editable.NEVER))
    {
      result = requested;
    }
    else if (able == Editable.WHEN_EXPANDED && (requested == Editable.WHEN_EXPANDED || requested == Editable.NEVER))
    {
      result = requested;
    }
    else
    {
      result = Editable.NEVER;
    }
    return result;
  }

  public void setProvider(final Provider<T> provider)
  {
    this.provider = provider;
    refreshDisplay();
  }

  public abstract void refreshDisplay();

  public void onTileExpanded()
  {
    this.expanded = true;
  }

  public void onTileContracted()
  {
    this.expanded = false;
  }

  public boolean hasControls()
  {
    return isExpandable() || isEditable();
  }

  @Override
  public LayoutParams getLayoutParams()
  {
    return (LayoutParams) super.getLayoutParams();
  }

  public static class LayoutParams extends ViewGroup.LayoutParams
  {
    public int top = -1;
    public int left = -1;
    public int colSpan = 1;
    public int rowSpan = 1;
    public boolean expandable = false;
    public Editable editable = Editable.NEVER;

    public LayoutParams()
    {
      this(MATCH_PARENT, MATCH_PARENT);
    }

    public LayoutParams(int width, int height)
    {
      super(MATCH_PARENT, MATCH_PARENT);
    }

    public LayoutParams(Context context, AttributeSet attrs)
    {
      super(context, attrs);
      final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Tile);
      top = a.getInt(R.styleable.Tile_tile_top, top);
      left = a.getInt(R.styleable.Tile_tile_left, left);
      colSpan = a.getInt(R.styleable.Tile_tile_colspan, colSpan);
      rowSpan = a.getInt(R.styleable.Tile_tile_rowspan, rowSpan);
      if (a.hasValue(R.styleable.Tile_tile_expandable))
      {
        expandable = a.getBoolean(R.styleable.Tile_tile_expandable, expandable);
      }
      final String editableStr = a.getString(R.styleable.Tile_tile_editable);
      if (editableStr != null)
      {
        editable = Editable.valueOf(editableStr);
      }
      a.recycle();
    }

    public LayoutParams(ViewGroup.LayoutParams params)
    {
      super(params);
      if (params instanceof LayoutParams)
      {
        top = ((LayoutParams) params).top;
        left = ((LayoutParams) params).left;
        colSpan = ((LayoutParams) params).colSpan;
        rowSpan = ((LayoutParams) params).rowSpan;
        expandable = ((LayoutParams) params).expandable;
        editable = ((LayoutParams) params).editable;
      }
    }
  }

  @Override
  public void addView(@Nonnull final View view)
  {
    super.addView(view);
    if (controlLayout != null && controlLayout != view)
    {
      controlLayout.bringToFront();
    }
  }

  // Control methods

  /**
   * @return {@code true} if the tile is currently expanded; otherwise {@code false}
   */
  public boolean isExpanded()
  {
    return expanded;
  }

  /**
   * @return {@code true} if the tile can be expanded and contracted; otherwise {@code false}
   */
  public boolean isExpandable()
  {
    return getLayoutParams().expandable;
  }

  /**
   * @return {@code true} if the tile can be edited in its current expansion state; otherwise {@code false}
   */
  public boolean isEditable()
  {
    final LayoutParams params = getLayoutParams();
    return (params.editable == Editable.ALWAYS ||
            (params.editable == Editable.WHEN_CONTRACTED && !expanded) ||
            (params.editable == Editable.WHEN_EXPANDED && expanded));
  }

  /**
   * @return {@code true} if the tile can be edited in any expansion state; otherwise {@code false}
   */
  public boolean isEverEditable()
  {
    return getLayoutParams().editable != Editable.NEVER;
  }

  public enum Editable
  {
    NEVER,
    ALWAYS,
    WHEN_CONTRACTED,
    WHEN_EXPANDED
  }

  /**
   * Work out if this tile will show information.  Useful to run when deciding whether to add a tile to a view or not.
   * Note that this uses the informaiton currently supplied by the provider; if there is no provider then this will return
   * {@code false}.
   * @return {@code true} if the tile will show information; otherwise {@code false}
   */
  public abstract boolean willShowInformation();
}
