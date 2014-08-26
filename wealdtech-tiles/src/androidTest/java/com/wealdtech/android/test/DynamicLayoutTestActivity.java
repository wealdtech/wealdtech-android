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
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.wealdtech.android.providers.Provider;
import com.wealdtech.android.test.providers.PresetTextProvider;
import com.wealdtech.android.test.tiles.TextTile;
import com.wealdtech.android.tiles.TileLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class DynamicLayoutTestActivity extends Activity
{
  private static final Logger LOG = LoggerFactory.getLogger(DynamicLayoutTestActivity.class);

  Provider<String> textProvider1, textProvider2, textProvider3;

  public DynamicLayoutTestActivity()
  {
    textProvider1 = new PresetTextProvider("Test 1");
    textProvider2 = new PresetTextProvider("Test 2");
    textProvider3 = new PresetTextProvider("Test 3");
  }

  private class ViewHolder
  {
    public TileLayout layout;
    public TextTile textTile1;
    public TextTile textTile2;
    public TextTile textTile3;
  }
  private ViewHolder holder = new ViewHolder();

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    holder.layout = new TileLayout(this);
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    holder.layout.setLayoutParams(layoutParams);
    setContentView(holder.layout);

    holder.textTile1 = new TextTile(this);
    textProvider1.addDataChangedListener(holder.textTile1);
    holder.textTile1.setColSpan(3);
    holder.layout.addView(holder.textTile1);

    holder.textTile2 = new TextTile(this);
    textProvider2.addDataChangedListener(holder.textTile2);
    holder.textTile2.setRowSpan(2);
    holder.textTile2.setColSpan(2);
    holder.layout.addView(holder.textTile2);

    holder.textTile3 = new TextTile(this);
    textProvider3.addDataChangedListener(holder.textTile3);
    holder.textTile3.setRowSpan(2);
    holder.layout.addView(holder.textTile3);
  }

  @Override
  public void onDestroy()
  {
    textProvider3.removeDataChangedListener(holder.textTile3);
    textProvider2.removeDataChangedListener(holder.textTile2);
    textProvider1.removeDataChangedListener(holder.textTile1);
    super.onDestroy();
  }
}
