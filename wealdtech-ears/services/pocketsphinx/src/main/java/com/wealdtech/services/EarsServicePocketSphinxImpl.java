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

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import edu.cmu.pocketsphinx.*;

import java.io.File;
import java.io.IOException;

import static android.widget.Toast.makeText;

/**
 *
 */
public class EarsServicePocketSphinxImpl extends Service implements RecognitionListener
{
  /* Named searches allow to quickly reconfigure the decoder */
  private static final String KWS_SEARCH = "wakeup";
  private static final String FORECAST_SEARCH = "forecast";
  private static final String DIGITS_SEARCH = "digits";
  private static final String PHONE_SEARCH = "phones";
  private static final String MENU_SEARCH = "menu";

  /* Keyword we are looking for to activate menu */
  private static final String KEYPHRASE = "oh mighty computer";

  private SpeechRecognizer recognizer;

  public EarsServicePocketSphinxImpl()
  {
    super();
  }

  @Override
  public void onBeginningOfSpeech() {
  }

  /**
   * We stop recognizer here to get a final result
   */
  @Override
  public void onEndOfSpeech() {
      if (!recognizer.getSearchName().equals(KWS_SEARCH))
          switchSearch(KWS_SEARCH);
  }

  @Override
  public void onPartialResult(final Hypothesis hypothesis)
  {
    if (hypothesis == null)
        return;

    String text = hypothesis.getHypstr();
    if (text.equals(KEYPHRASE))
        switchSearch(MENU_SEARCH);
    else if (text.equals(DIGITS_SEARCH))
        switchSearch(DIGITS_SEARCH);
    else if (text.equals(PHONE_SEARCH))
        switchSearch(PHONE_SEARCH);
    else if (text.equals(FORECAST_SEARCH))
        switchSearch(FORECAST_SEARCH);
    else
      makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
  }

  private void switchSearch(String searchName) {
      recognizer.stop();

      // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
      if (searchName.equals(KWS_SEARCH))
          recognizer.startListening(searchName);
      else
          recognizer.startListening(searchName, 10000);

//      String caption = getResources().getString(captions.get(searchName));
//      ((TextView) findViewById(R.id.caption_text)).setText(caption);
  }

  /**
   * This callback is called when we stop the recognizer.
   */
  @Override
  public void onResult(Hypothesis hypothesis) {
//      ((TextView) findViewById(R.id.result_text)).setText("");
      if (hypothesis != null) {
          String text = hypothesis.getHypstr();
          makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
      }
  }


  private void setupRecognizer(File assetsDir) throws IOException {
      // The recognizer can be configured to perform multiple searches
      // of different kind and switch between them

      recognizer = SpeechRecognizerSetup.defaultSetup()
              .setAcousticModel(new File(assetsDir, "en-us-ptm"))
              .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

              .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
              .setKeywordThreshold(1e-45f) // Threshold to tune for keyphrase to balance between false alarms and misses
              .setBoolean("-allphone_ci", true)  // Use context-independent phonetic search, context-dependent is too slow for mobile


              .getRecognizer();
      recognizer.addListener(this);


      /**
       * In your application you might not need to add all those searches.
       * They are added here for demonstration. You can leave just one.
       */

      // Create keyword-activation search.
      recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

      // Create grammar-based search for selection between demos
      File menuGrammar = new File(assetsDir, "menu.gram");
      recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

      // Create grammar-based search for digit recognition
      File digitsGrammar = new File(assetsDir, "digits.gram");
      recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);

      // Create language model search
      File languageModel = new File(assetsDir, "weather.dmp");
      recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);

      // Phonetic search
      File phoneticModel = new File(assetsDir, "en-phone.dmp");
      recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);
  }

  @Override
  public void onError(Exception error) {
    makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onTimeout() {
      switchSearch(KWS_SEARCH);
  }

//  @Override
//  public void startListening(final String prompt)
//  {
//    super.startListening(prompt);
//  }
//
//  @Override
//  public void stopListening()
//  {
//    super.stopListening();
//  }

  @Nullable
  @Override
  public IBinder onBind(final Intent intent)
  {
    return null;
  }

  @Override
  public void onCreate()
  {
    super.onCreate();

    Log.e("weald", "onCreate()");

    new AsyncTask<Void, Void, Exception>() {
      @Override
      protected Exception doInBackground(Void... params) {
        try {
          Assets assets = new Assets(getApplicationContext());
          File assetDir = assets.syncAssets();
          setupRecognizer(assetDir);
        } catch (IOException e) {
          return e;
        }
        return null;
      }

      @Override
      protected void onPostExecute(Exception result) {
        if (result != null) {
          makeText(getApplicationContext(), "Failed to init recognizer " + result, Toast.LENGTH_SHORT).show();
          //                ((TextView) findViewById(R.id.caption_text))
          //                        .setText("Failed to init recognizer " + result);
        } else {
          switchSearch(KWS_SEARCH);
        }
      }
    }.execute();
  }

  @Override
  public void onDestroy()
  {
    Log.e("weald", "onDestroy");
    super.onDestroy();
  }

  @Override
  public int onStartCommand(final Intent intent, final int flags, final int startId)
  {
    Log.e("weald", "onStartCommand");
    return START_STICKY;
  }
}
