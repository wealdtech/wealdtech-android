/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.roberto;

/**
 */
public class Roberto
{

  public static Roberto getInstance()
  {
    return new Roberto();
  }

  /**
   * Carry out a check to see if the service is available.
   * This carries out a full end-to-end test of the service at the time it is called, however this provides no guarantee that the
   * service will continue to be available in the future
   * @return {@code true} if the service is available, otherwise {@code false}
   */
  public boolean isServiceAvailable()
  {
    return true;
  }
}
