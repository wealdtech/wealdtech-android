/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import com.wealdtech.android.test.tiles.ClockTile;
import com.wealdtech.android.tiles.TileLayout;
import com.wealdtech.android.utils.ViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class PeriodicUpdateTestActivity extends Activity
{
  private static final Logger LOG = LoggerFactory.getLogger(PeriodicUpdateTestActivity.class);

  public PeriodicUpdateTestActivity()
  {
  }

  public class ViewHolder
  {
    public TileLayout layout;
    public ClockTile clockTile1;
    public ClockTile clockTile2;
  }
  public ViewHolder holder = new ViewHolder();

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    holder.layout = new TileLayout(this);
    holder.layout.setId(ViewUtils.generateViewId());
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    holder.layout.setLayoutParams(layoutParams);
    setContentView(holder.layout);

    holder.clockTile1 = new ClockTile(this);
    holder.clockTile1.setId(ViewUtils.generateViewId());
    holder.layout.addView(holder.clockTile1);

    holder.clockTile2 = new ClockTile(this);
    holder.clockTile2.setId(ViewUtils.generateViewId());
    holder.layout.addView(holder.clockTile2);
  }
}
