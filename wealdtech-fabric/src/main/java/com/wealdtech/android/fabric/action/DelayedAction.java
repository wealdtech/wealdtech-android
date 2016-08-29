/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric.action;

import android.os.AsyncTask;
import com.wealdtech.android.fabric.Rule;

/**
 * An action that delays for a certain amount of time prior to triggering.  If the action is called again then before the delay
 * completes then it is cancelled and the new action is started.
 */
public class DelayedAction extends Action
{
  private final Action action;

  private static final Long DEFAULT_DELAY = 250L;

  private long delayInMs;
  private WaitTask lastWaitTask;

  private DelayedAction(final Action action, final long delayInMs)
  {
    this.action = action;
    this.delayInMs = delayInMs;
  }

  public void act(final Rule rule)
  {
    synchronized (this)
    {
      if (lastWaitTask != null)
      {
        lastWaitTask.cancel(true);
      }
      lastWaitTask = new WaitTask(rule, action);
      lastWaitTask.execute();
    }
  }

  public static Action delayedAction(final Action action)
  {
    return new DelayedAction(action, DEFAULT_DELAY);
  }

  public static Action delayedAction(final Action action, final long delayInMs)
  {
    return new DelayedAction(action, delayInMs);
  }

  private class WaitTask extends AsyncTask<Void, Void, Void>
  {
    private final Rule rule;
    private final Action action;

    public WaitTask(final Rule rule, final Action action)
    {
      this.rule = rule;
      this.action = action;
    }
    @Override
    protected Void doInBackground(final Void... params)
    {
      try
      {
        Thread.sleep(delayInMs);
      }
      catch (final InterruptedException ignored) {}
      return null;
    }

    @Override
    protected void onPostExecute(final Void result)
    {
      action.act(rule);
      lastWaitTask = null;
    }
  }
}
