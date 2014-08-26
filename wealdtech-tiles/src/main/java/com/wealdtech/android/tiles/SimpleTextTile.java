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
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wealdtech.android.providers.DataChangedListener;
import com.wealdtech.android.utils.ViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A tile which shows simple text
 */
public class SimpleTextTile extends Tile<String> implements DataChangedListener<String>
{
  private static final Logger LOG = LoggerFactory.getLogger(SimpleTextTile.class);

  private static final Editable EDITABLE = Editable.NEVER;
  private static final boolean EXPANDABLE = true;

  public static class ViewHolder
  {
    public TextView text;
  }

  public final ViewHolder holder = new ViewHolder();

  public SimpleTextTile(final Context context)
  {
    this(context, null, 0);
  }

  public SimpleTextTile(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public SimpleTextTile(final Context context, final AttributeSet attrs, final int defStyle)
  {
    super(context, attrs, defStyle);
    this.setEditable(EDITABLE);
    this.setExpandable(EXPANDABLE);

    holder.text = new TextView(context);
    holder.text.setId(ViewUtils.generateViewId());
    holder.text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                           ViewGroup.LayoutParams.MATCH_PARENT));
    addView(holder.text);
  }

  @Override
  public void onDataChanged(final String data)
  {
    refreshDisplay(data);
  }

  @Override
  protected void refreshDisplay(final String data)
  {
    holder.text.setText(data);
  }

  @Override
  public void onTileExpanded()
  {
    holder.text.setTextColor(Color.BLUE);
  }

  @Override
  public void onTileContracted()
  {
    holder.text.setTextColor(Color.WHITE);
  }
}