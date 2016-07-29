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
 * An action that delays for a certain amount of time prior to triggering.  It only triggers if another delayed action has
 * not started during the time that it was waiting
 */
public class DelayedAction extends Action
{
  private final Action action;

  private int seq = 0;

  private long delayInMs;

  private int getSeq(){ return seq; }

  private DelayedAction(final Action action, final long delayInMs)
  {
    this.action = action;
    this.delayInMs = delayInMs;
  }

  public void act(final Rule rule)
  {
    new AsyncTask<Integer, Void, Integer>()
    {
      @Override
      protected Integer doInBackground(final Integer... integers)
      {
        try {Thread.sleep(delayInMs); } catch (final InterruptedException ignored) {}
        return integers[0];
      }

      @Override
      protected void onPostExecute(final Integer seq)
      {
        if (getSeq() == seq)
        {
          // No additional events since; do the work
          action.act(rule);
        }
      }
    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ++seq);
    seq %= Integer.MAX_VALUE;
  }

  public static Action delayedAction(final Action action, final long delayInMs)
  {
    return new DelayedAction(action, delayInMs);
  }
}
