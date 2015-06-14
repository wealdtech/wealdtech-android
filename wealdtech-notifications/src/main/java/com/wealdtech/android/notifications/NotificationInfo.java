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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Optional;
import com.wealdtech.WID;
import com.wealdtech.WObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.wealdtech.Preconditions.checkState;

/**
 * Information for a notification
 */
public class NotificationInfo extends WObject<NotificationInfo> implements Comparable<NotificationInfo>
{
  private static final Logger LOG = LoggerFactory.getLogger(NotificationInfo.class);

  private static final String GROUP = "group";
  private static final String TITLE = "title";
  private static final String SUMMARY = "summary";
  private static final String CONTENT = "content";
//  private static final String HANDLER = "handler";

  @JsonCreator
  public NotificationInfo(final Map<String, Object> data){super(data);}

  @Override
  protected Map<String, Object> preCreate(final Map<String, Object> data)
  {
    if (!data.containsKey(ID)) { data.put(ID, WID.generate()); }
    if (!data.containsKey(GROUP)) { data.put(GROUP, "default"); }

    return data;
  }

  @Override
  protected void validate()
  {
    checkState(exists(ID), "Notification info failed validation: must contain ID (" + getAllData() + ")");
    checkState(exists(GROUP), "Notification info failed validation: must contain group (" + getAllData() + ")");
//    checkState(exists(HANDLER), "Notification info failed validation: must contain handler (" + getAllData() + ")");
//    checkState(getHandler() != null, "Notification info failed validation: handler is invalid (" + getAllData() + ")");
  }

  // We override getId() to make it non-null as we confirm ID's existence in validate()
  @Override
  @Nonnull
  @JsonIgnore
  public WID<NotificationInfo> getId(){ return super.getId(); }

  @JsonIgnore
  public Optional<String> getTitle() { return get(TITLE, String.class); }

  @JsonIgnore
  public Optional<String> getSummary() { return get(SUMMARY, String.class); }

  @JsonIgnore
  public Optional<String> getContent() { return get(CONTENT, String.class); }

  @JsonIgnore
  public String getGroup(){ return get(GROUP, String.class).get(); }

//  @JsonIgnore
//  public NotificationHandler getHandler()
//  {
//    final String handlerName = get(HANDLER, String.class).get();
//    try
//    {
//      return (NotificationHandler)Class.forName(handlerName).newInstance();
//    }
//    catch (final ClassNotFoundException cnfe)
//    {
//      LOG.error("Unknown alarm handler class {} ", handlerName, cnfe);
//      return null;
//    }
//    catch (final InstantiationException | IllegalAccessException e)
//    {
//      LOG.error("Unable to instantiate alarm handler class {} ", handlerName, e);
//      return null;
//    }
//  }

  // Builder boilerplate
  public static class Builder<P extends Builder<P>> extends WObject.Builder<NotificationInfo, P>
  {
    public Builder()
    {
      super();
    }

    public Builder(final NotificationInfo prior)
    {
      super(prior);
    }

    public P title(final String title)
    {
      data(TITLE, title);
      return self();
    }

    public P summary(final String summary)
    {
      data(SUMMARY, summary);
      return self();
    }

    public P content(final String content)
    {
      data(CONTENT, content);
      return self();
    }

    public P group(final String group)
    {
      data(GROUP, group);
      return self();
    }

//    public P handler(final Class<? extends NotificationHandler> handler)
//    {
//      data(HANDLER, handler.getCanonicalName());
//      return self();
//    }

    public NotificationInfo build()
    {
      return new NotificationInfo(data);
    }
  }

  public static Builder<?> builder()
  {
    return new Builder();
  }

  public static Builder<?> builder(final NotificationInfo prior)
  {
    return new Builder(prior);
  }

}
