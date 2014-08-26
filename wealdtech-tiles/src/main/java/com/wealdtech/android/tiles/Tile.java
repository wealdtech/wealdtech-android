/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * A Tile is a UI element which is of a fixed ratio of width to height and designed to work within a TileLayout.
 */
public abstract class Tile<T> extends FrameLayout
{
  private static final Logger LOG = LoggerFactory.getLogger(Tile.class);

  public static final int FLOATING = -1;

  /** The controls for the tile */
  protected TextView controlLayout;

  /** If the tile is currently expanded */
  private boolean expanded = false;

  /** The position of the leftmost edge of the tile */
  private int left = FLOATING;
  /** The position of the topmost edge of the tile */
  private int top = FLOATING;
  /** The number of columns the tile spans */
  private int colSpan = 1;
  /** The number of rows the tile spans */
  private int rowSpan = 1;
  /** Under which circumstances the tile is editable */
  private Editable editable = Editable.NEVER;
  /** If the tile can be expanded */
  private boolean expandable = false;

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
    this(context, attrs, 0, Editable.NEVER, false);
  }

  public Tile(final Context context,
              final AttributeSet attrs,
              final int defStyle,
              final Editable editable,
              final boolean expandable)
  {
    super(context, attrs, defStyle);
    this.editable = editable;
    this.expandable = expandable;
    setAttrs(context, attrs, defStyle);
    initControlView();
  }

  private void initControlView()
  {
    // If we already had one then remove it
    if (controlLayout != null)
    {
      removeView(controlLayout);
    }

    if (hasControls())
    {
      controlLayout = new TextView(getContext());
      controlLayout.setText("+");
      controlLayout.setBackgroundColor(Color.BLUE);
      controlLayout.setTextColor(Color.GREEN);
      controlLayout.setTextSize(24);

      controlLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                               ViewGroup.LayoutParams.WRAP_CONTENT,
                                                               Gravity.BOTTOM | Gravity.RIGHT));
      addView(controlLayout);
    }
    else
    {
      // No controls
      controlLayout = null;
    }
  }

  private void setAttrs(final Context context, final AttributeSet attrs, final int defStyle)
  {
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Tile);
    final int numAttrs = a.getIndexCount();
    for (int i = 0; i < numAttrs; ++i)
    {
      int attr = a.getIndex(i);
      if (attr == R.styleable.Tile_tile_top)
      {
        top = a.getInteger(attr, top);
      }
      else if (attr == R.styleable.Tile_tile_left)
      {
        left = a.getInteger(attr, left);
      }
      else if (attr == R.styleable.Tile_tile_colspan)
      {
        colSpan = a.getInteger(attr, colSpan);
      }
      else if (attr == R.styleable.Tile_tile_rowspan)
      {
        rowSpan = a.getInteger(attr, rowSpan);
      }
      else if (attr == R.styleable.Tile_tile_expandable)
      {
        expandable = calcExpandable(expandable, a.getBoolean(attr, expandable));
      }
      else if (attr == R.styleable.Tile_tile_editable)
      {
        final String editableStr = a.getString(attr);
        if (editableStr != null)
        {
          editable = calcEditable(editable, Editable.valueOf(editableStr));
        }
      }
    }
    a.recycle();
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

  protected abstract void refreshDisplay(final T data);

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

//  @Override
//  public void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
//  {
//    // TODO Tiles are always the full size of the available space (minus margins and padding); spec passed in should always be EXACTLY, so we shouldn't pass this
//    // up to the parent to do the work as it over-calculates
//    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//    int width = MeasureSpec.getSize(widthMeasureSpec);
//    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//    int height = MeasureSpec.getSize(heightMeasureSpec);
//    LOG.error("onMeasure({}, {}) / ({}, {})         AT_MOST:{} EXACTLY:{} UNSPECIFIED:{}", width, height, widthMode, heightMode, MeasureSpec.AT_MOST, MeasureSpec.EXACTLY, MeasureSpec.UNSPECIFIED);
//    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//  }

  @Override
  public void addView(@Nonnull final View view)
  {
    super.addView(view);
    if (controlLayout != null && controlLayout != view)
    {
      controlLayout.bringToFront();
    }
  }

  @Override
  public void removeAllViews()
  {
    //... except our control layout, if we have one
    super.removeAllViews();
    if (controlLayout != null)
    {
      addView(controlLayout);
    }
  }

  // Control methods

  /**
   * @return the position of the leftmost edge of the tile.  Can be {@code FLOATING}
   */
  public int getTileLeft()
  {
    return this.left;
  }

  /**
   * Set the position of the leftmost edge of the tile
   *
   * @param left the position.  Can be {@code TILE_FLOATING}
   */
  public void setTileLeft(final int left)
  {
    this.left = left;
  }

  /**
   * @return the position of the topmost edge of the tile.  Can be {@code FLOATING}
   */
  public int getTileTop()
  {
    return this.top;
  }

  /**
   * Set the position of the topmost edge of the tile
   *
   * @param top the position.  Can be {@code TILE_FLOATING}
   */
  public void setTileTop(final int top)
  {
    this.top = top;
  }

  /**
   * @return the number of columns this tile covers.
   */
  public int getColSpan()
  {
    return this.colSpan;
  }

  /**
   * Set the number of columns this tile covers.
   *
   * @param colSpan the number of columns this tile covers.  Must be greater than 0
   */
  public void setColSpan(final int colSpan)
  {
    if (colSpan < 1)
    {
      throw new RuntimeException("Tile column span must be at least 1");
    }
    this.colSpan = colSpan;
  }

  /**
   * @return the number of rows this tile covers.
   */
  public int getRowSpan()
  {
    return this.rowSpan;
  }

  /**
   * Set the number of rows this tile covers.
   *
   * @param rowSpan the number of columns this tile covers.  Must be greater than 0
   */
  public void setRowSpan(final int rowSpan)
  {
    if (rowSpan < 1)
    {
      throw new RuntimeException("Tile row span must be at least 1");
    }
    this.rowSpan = rowSpan;
  }

  public boolean isFloating()
  {
    return top == FLOATING && left == FLOATING;
  }

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
    return expandable;
  }

  public void setExpandable(final boolean expandable)
  {
    this.expandable = expandable;
    initControlView();
  }

  /**
   * @return {@code true} if the tile can be edited in its current expansion state; otherwise {@code false}
   */
  public boolean isEditable()
  {
    return (editable == Editable.ALWAYS ||
            (editable == Editable.WHEN_CONTRACTED && !expanded) ||
            (editable == Editable.WHEN_EXPANDED && expanded));
  }

  public void setEditable(final Editable editable)
  {
    this.editable = editable;
    initControlView();
  }

  /**
   * @return {@code true} if the tile can be edited in any expansion state; otherwise {@code false}
   */
  public boolean isEverEditable()
  {
    return editable != Editable.NEVER;
  }

  public enum Editable
  {
    NEVER,
    ALWAYS,
    WHEN_CONTRACTED,
    WHEN_EXPANDED
  }

  /**
   * Work out if this tile will show information.  Useful to run prior to instantiating a tile to decide if it's worth it or not
   *
   * @return {@code true} if the tile will show information; otherwise {@code false}
   */
  public static <T> boolean willShowInformation(final T data)
  {
    return data != null;
  }
}
