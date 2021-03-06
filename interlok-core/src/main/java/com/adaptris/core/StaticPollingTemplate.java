/*
 * Copyright 2017 Adaptris Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.adaptris.core;

import static org.apache.commons.lang.StringUtils.isEmpty;

import com.adaptris.annotation.MarshallingCDATA;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Replaces {@link PollingTrigger#setTemplate(String)}.
 * 
 * @config static-polling-trigger-template
 */
@XStreamAlias("static-polling-trigger-template")
public class StaticPollingTemplate implements PollingTrigger.MessageProvider {

  @MarshallingCDATA
  private String template;

  public StaticPollingTemplate() {

  }

  public StaticPollingTemplate(String s) {
    this();
    setTemplate(s);
  }

  @Override
  public void init() throws CoreException {
  }

  @Override
  public void start() throws CoreException {
  }

  @Override
  public void stop() {
  }

  @Override
  public void close() {
  }

  /**
   *
   * @param s the template message to use
   */
  public void setTemplate(String s) {
    template = s;
  }

  /**
   *
   * @return the template message to use
   */
  public String getTemplate() {
    return template;
  }

  @Override
  public AdaptrisMessage createMessage(AdaptrisMessageFactory fac) {
    return fac.newMessage(isEmpty(getTemplate()) ? "" : getTemplate());
  }

}
