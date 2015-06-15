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
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Optional;
import com.wealdtech.WID;
import com.wealdtech.WObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.wealdtech.Preconditions.checkState;

/**
 * Abstract template for a notification
 */
public abstract class AbstractNotificationTemplate extends WObject<AbstractNotificationTemplate> implements NotificationTemplate
{
  private static final Logger LOG = LoggerFactory.getLogger(AbstractNotificationTemplate.class);

  private static final String SMALL_ICON_RES_ID = "smalliconresid";
  private static final String LARGE_ICON_RES_ID = "largeiconresid";
  private static final String SOUND_RES_ID = "soundresid";
  private static final String FOLLOW_ON_ACTION = "followownaction";

  @JsonCreator
  public AbstractNotificationTemplate(final Map<String, Object> data){super(data);}

  @Override
  protected Map<String, Object> preCreate(final Map<String, Object> data)
  {
    if (!data.containsKey(ID)) { data.put(ID, WID.generate()); }
    if (!data.containsKey(FOLLOW_ON_ACTION)) { data.put(FOLLOW_ON_ACTION, FollowOnAction.UPDATE); }

    return data;
  }

  @Override
  protected void validate()
  {
    checkState(exists(ID), "Notification template failed validation: must contain ID (" + getAllData() + ")");
  }

//  // We override getId() to make it non-null as we confirm ID's existence in validate()
//  @Override
//  @Nonnull
//  @JsonIgnore
//  public WID<T> getId(){ return super.getId(); }

  @JsonIgnore
  public Optional<Integer> getSmallIconResId() { return get(SMALL_ICON_RES_ID, Integer.class); }

  @JsonIgnore
  public Optional<Integer> getLargeIconResId() { return get(LARGE_ICON_RES_ID, Integer.class); }

  @JsonIgnore
  public Optional<Integer> getSoundResId() { return get(SOUND_RES_ID, Integer.class); }

  @JsonIgnore
  public FollowOnAction getFollowOnAction() { return get(FOLLOW_ON_ACTION, FollowOnAction.class).get(); }

  // Builder boilerplate
  public static class Builder<P extends Builder<P>> extends WObject.Builder<AbstractNotificationTemplate, P>
  {
    public Builder()
    {
      super();
    }

    public Builder(final AbstractNotificationTemplate prior)
    {
      super(prior);
    }

    public P smallIconResId(final Integer smallIconResId)
    {
      data(SMALL_ICON_RES_ID, smallIconResId);
      return self();
    }

    public P largeIconResId(final Integer largeIconResId)
    {
      data(LARGE_ICON_RES_ID, largeIconResId);
      return self();
    }

    public P soundResId(final Integer soundResId)
    {
      data(SOUND_RES_ID, soundResId);
      return self();
    }

    public P followOnAction(final FollowOnAction followOnAction)
    {
      data(FOLLOW_ON_ACTION, followOnAction);
      return self();
    }
  }

  @Override
  public abstract Notification generate(final Context context, final NotificationInfo info);
}
