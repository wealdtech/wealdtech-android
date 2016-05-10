/*
 * Copyright 2012 - 2016 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.services;

import android.content.Intent;
import android.speech.RecognizerIntent;
import com.wealdtech.ServerError;

import java.util.Locale;

/**
 *
 */
public class EarsServiceGoogleImpl extends AbstractEarsService
{
  private final int REQ_CODE_SPEECH_INPUT = 12345;

  public EarsServiceGoogleImpl()
  {
    super();
  }


  @Override
  public void startListening(final String prompt)
  {
    final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    if (prompt != null)
    {
      intent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);
    }
//            try {
//                context.startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
//            } catch (ActivityNotFoundException a) {
//                Toast.makeText(context,
//                               getString(R.string.speech_not_supported),
//                               Toast.LENGTH_SHORT).show();
//            }
  }

  @Override
  public void stopListening()
  {
    // TODO implement
    throw new ServerError("Not implemented");
  }

  @Override
  public void addCallback(final EarsCallback callback)
  {
    // TODO implement
    throw new ServerError("Not implemented");
  }

  @Override
  public void removeCallback(final EarsCallback callback)
  {
    // TODO implement
    throw new ServerError("Not implemented");
  }
}
