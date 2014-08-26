/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.test.tiles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.wealdtech.android.providers.DataChangedListener;
import com.wealdtech.android.tiles.Tile;
import com.wealdtech.android.utils.ViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A tile which shows text
 */
public class MultiTextTile extends Tile<String> implements DataChangedListener<String>
{
  private static final Logger LOG = LoggerFactory.getLogger(MultiTextTile.class);

  private static final Editable EDITABLE = Editable.NEVER;
  private static final boolean EXPANDABLE = true;

  private static class ViewHolder
  {
    public TextView text1;
    public TextView text2;
    public TextView text3;
    public TextView text4;
  }
  private final ViewHolder holder = new ViewHolder();

  public MultiTextTile(final Context context)
  {
    this(context, null, 0);
  }

  public MultiTextTile(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public MultiTextTile(final Context context, final AttributeSet attrs, final int defStyle)
  {
    super(context, attrs, defStyle, EDITABLE, EXPANDABLE);

    // FIXME build system is not picking up the correct R; need to find out what is going on and fix it
    final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    inflater.inflate(R.layout.test_multi_text, this, true);
//    holder.text1 = (TextView)findViewById(R.id.test_multi_text_1);
//    holder.text2 = (TextView)findViewById(R.id.test_multi_text_2);
//    holder.text3 = (TextView)findViewById(R.id.test_multi_text_3);
//    holder.text4 = (TextView)findViewById(R.id.test_multi_text_4);
    holder.text1 = new TextView(getContext());
    holder.text1.setId(ViewUtils.generateViewId());
    holder.text2 = new TextView(getContext());
    holder.text2.setId(ViewUtils.generateViewId());
    holder.text3 = new TextView(getContext());
    holder.text3.setId(ViewUtils.generateViewId());
    holder.text4 = new TextView(getContext());
    holder.text4.setId(ViewUtils.generateViewId());
  }

  @Override
  public void onDataChanged(final String data)
  {
    refreshDisplay(data);
  }

  @Override
  protected void refreshDisplay(final String data)
  {
    holder.text1.setText(data);
    holder.text2.setText(data);
    holder.text3.setText(data);
    holder.text4.setText(data);
  }
}
