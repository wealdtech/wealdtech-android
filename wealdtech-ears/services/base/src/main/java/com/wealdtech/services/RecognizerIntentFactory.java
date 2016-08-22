package com.wealdtech.services;

import android.content.Intent;
import android.speech.RecognizerIntent;

import java.util.Locale;

/**
 * Created by jgm on 10/05/16.
 */
public class RecognizerIntentFactory
{
  public static final int ACTION_GET_LANGUAGE_DETAILS_REQUEST_CODE = 88811;

  private static final int DEFAUT_MAX_RESULTS = 100;

  public static Intent getSimpleRecognizerIntent(String prompt)
  {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);
    return intent;
  }

  public static Intent getBlankRecognizeIntent()
  {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    return intent;
  }

  public static Intent getWebSearchRecognizeIntent()
  {
    Intent intent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
    return intent;
  }

  public static Intent getHandsFreeRecognizeIntent()
  {
    Intent intent = new Intent(RecognizerIntent.ACTION_VOICE_SEARCH_HANDS_FREE);
    return intent;
  }

  public static Intent getPossilbeWebSearchRecognizeIntent(String prompt)
  {
    Intent intent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);
    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, DEFAUT_MAX_RESULTS);
    intent.putExtra(RecognizerIntent.EXTRA_WEB_SEARCH_ONLY, false);
    intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
    //        intent.putExtra(RecognizerIntent.ORIGIN, "http://www.github.com/gast-lib");
    return intent;
  }

  public static Intent getLanguageDetailsIntent()
  {
    Intent intent = new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
    return intent;
  }

}
