/*
 * Copyright 2012 - 2015 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.notifications;

import android.app.Notification;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * A simple notification template
 */
public class BaseNotificationTemplate extends AbstractNotificationTemplate
{
  private static final Logger LOG = LoggerFactory.getLogger(BaseNotificationTemplate.class);

  @JsonCreator
  public BaseNotificationTemplate(final Map<String, Object> data){super(data);}

  // Builder boilerplate
  public static class Builder<P extends Builder<P>> extends AbstractNotificationTemplate.Builder<P>
  {
    public Builder()
    {
      super();
    }

    public Builder(final BaseNotificationTemplate prior)
    {
      super(prior);
    }

    public BaseNotificationTemplate build()
    {
      return new BaseNotificationTemplate(data);
    }
  }

  public static Builder<?> builder()
  {
    return new Builder();
  }

  public static Builder<?> builder(final BaseNotificationTemplate prior)
  {
    return new Builder(prior);
  }

  @Override
  public Notification generate(final Context context, final NotificationInfo info)
  {
    final NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setAutoCancel(true);

    if (getLargeIconResId().isPresent())
    {
      builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), getLargeIconResId().get()));
    }
    if (getSmallIconResId().isPresent())
    {
      builder.setSmallIcon(getSmallIconResId().get());
    }
    if (getSoundResId().isPresent())
    {
      final Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/");
      builder.setSound(soundUri);
    }

    builder.setGroup(info.getGroup());
    if (info.getTitle().isPresent())
    {
      builder.setContentTitle(info.getTitle().get());
    }
    if (info.getContent().isPresent())
    {
      builder.setContentInfo(info.getContent().get());
    }

    return builder.build();
  }
}
