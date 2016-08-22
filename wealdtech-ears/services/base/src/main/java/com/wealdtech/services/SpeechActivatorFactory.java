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

import android.content.Context;
import com.wealdtech.android.ears.services.R;

/**
 *
 */
public class SpeechActivatorFactory
{
    public static SpeechActivator createSpeechActivator(Context context,
            SpeechActivationListener callback, String type)
    {
        SpeechActivator speechActivator = null;

        if (type.equals(context.getResources().getString(R.string.speech_activation_button)))
        {
            speechActivator = null;
        }
        else if (type.equals(context.getResources().getString(R.string.speech_activation_speak)))
        {
            speechActivator = new WordActivator(context, callback, "hello");
        }
        return speechActivator;
    }

    public static String getLabel(Context context, SpeechActivator speechActivator)
    {
        String label = "";
        if (speechActivator == null)
        {
            label = context.getString(R.string.speech_activation_button);
        }
        else if (speechActivator instanceof WordActivator)
        {
            label = context.getString(R.string.speech_activation_speak);
        }
        else
        {
            label = context.getString(R.string.speech_activation_button);
        }
        return label;
    }

}