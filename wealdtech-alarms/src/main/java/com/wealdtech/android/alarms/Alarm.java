/*
 * Copyright 2012 - 2015 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.alarms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wealdtech.WID;
import com.wealdtech.WObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.wealdtech.Preconditions.checkState;

/**
 */
public class Alarm extends WObject<Alarm> implements Comparable<Alarm>
{
  private static final Logger LOG = LoggerFactory.getLogger(Alarm.class);

  private static final String TIMESTAMP = "timestamp";
  private static final String GROUP = "group";
  private static final String HANDLER = "handler";

  @JsonCreator
  public Alarm(final Map<String, Object> data){super(data);}

  @Override
  protected Map<String, Object> preCreate(final Map<String, Object> data)
  {
    if (!data.containsKey(ID)) { data.put(ID, WID.generate()); }
    if (!data.containsKey(GROUP)) { data.put(GROUP, 0); }

    return data;
  }

  @Override
  protected void validate()
  {
    checkState(exists(ID), "Alarm failed validation: must contain ID (" + getAllData() + ")");
    checkState(exists(TIMESTAMP), "Alarm failed validation: must contain timestamp (" + getAllData() + ")");
    checkState(exists(GROUP), "Alarm failed validation: must contain group (" + getAllData() + ")");
    checkState(exists(HANDLER), "Alarm failed validation: must contain handler (" + getAllData() + ")");
    checkState(getHandler() != null, "Alarm failed validation: handler is invalid (" + getAllData() + ")");
  }

  // We override getId() to make it non-null as we confirm ID's existence in validate()
  @Override
  @Nonnull
  @JsonIgnore
  public WID<Alarm> getId(){ return super.getId(); }

  @JsonIgnore
  public Long getTimestamp() { return get(TIMESTAMP, Long.class).get(); }

  @JsonIgnore
  public Integer getGroup(){ return get(GROUP, Integer.class).get(); }

  @JsonIgnore
  public AlarmHandler getHandler()
  {
    final String handlerName = get(HANDLER, String.class).get();
    try
    {
      return (AlarmHandler)Class.forName(handlerName).newInstance();
    }
    catch (final ClassNotFoundException cnfe)
    {
      LOG.error("Unknown alarm handler class {} ", handlerName, cnfe);
      return null;
    }
    catch (final InstantiationException | IllegalAccessException e)
    {
      LOG.error("Unable to instantiate alarm handler class {} ", handlerName, e);
      return null;
    }
  }

  // Builder boilerplate
  public static class Builder<P extends Builder<P>> extends WObject.Builder<Alarm, P>
  {
    public Builder()
    {
      super();
    }

    public Builder(final Alarm prior)
    {
      super(prior);
    }

    public P timestamp(final Long timestamp)
    {
      data(TIMESTAMP, timestamp);
      return self();
    }

    public P group(final int group)
    {
      data(GROUP, group);
      return self();
    }

    public P handler(final Class<? extends AlarmHandler> handler)
    {
      data(HANDLER, handler.getCanonicalName());
      return self();
    }

    public Alarm build()
    {
      return new Alarm(data);
    }
  }

  public static Builder<?> builder()
  {
    return new Builder();
  }

  public static Builder<?> builder(final Alarm prior)
  {
    return new Builder(prior);
  }

}
