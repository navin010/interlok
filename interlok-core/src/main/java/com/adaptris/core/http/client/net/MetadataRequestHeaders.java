/*
 * Copyright 2015 Adaptris Ltd.
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

package com.adaptris.core.http.client.net;

import java.net.HttpURLConnection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.MetadataCollection;
import com.adaptris.core.MetadataElement;
import com.adaptris.core.http.client.RequestHeaderProvider;
import com.adaptris.core.metadata.MetadataFilter;
import com.adaptris.core.util.Args;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Implementation of {@link RequestHeaderProvider} that applies {@link com.adaptris.core.AdaptrisMessage} metadata as
 * headers to a {@link HttpURLConnection}.
 * 
 * @config http-metadata-request-headers
 * 
 */
@XStreamAlias("http-metadata-request-headers")
public class MetadataRequestHeaders implements RequestHeaderProvider<HttpURLConnection> {
  protected transient Logger log = LoggerFactory.getLogger(this.getClass());
  @NotNull
  @Valid
  private MetadataFilter filter;

  @AdvancedConfig
  @InputFieldDefault(value = "false")
  private Boolean unfold;

  public MetadataRequestHeaders() {
  }

  public MetadataRequestHeaders(MetadataFilter mf) {
    this();
    setFilter(mf);
  }

  @Override
  public HttpURLConnection addHeaders(AdaptrisMessage msg, HttpURLConnection target) {
    MetadataCollection metadataSubset = getFilter().filter(msg);
    for (MetadataElement me : metadataSubset) {
      String value = unfold(me.getValue());
      log.trace("Adding Request Property [{}: {}]", me.getKey(), value);
      target.addRequestProperty(me.getKey(), value);
    }
    return target;
  }

  public MetadataFilter getFilter() {
    return filter;
  }

  /**
   * Set the filter to be applied to metadata before adding as request properties.
   * 
   * @param mf the filter.
   */
  public void setFilter(MetadataFilter mf) {
    this.filter = Args.notNull(mf, "metadata filter");
  }

  public Boolean getUnfold() {
    return unfold;
  }

  /**
   * Unfold headers onto a single line.
   * <p>
   * RFC7230 deprecates the folding of headers onto multiple lines; so HTTP headers are expected to be single line. This param
   * allows you to enforce that unfolding metadata values happens before writing them as request properties.
   * </p>
   * 
   * @param b true to unfold values (default is false to preserve legacy behaviour).
   */
  public void setUnfold(Boolean b) {
    this.unfold = b;
  }

  boolean unfold() {
    return BooleanUtils.toBooleanDefaultIfNull(getUnfold(), false);
  }

  String unfold(String s) {
    if (unfold()) {
      return s.replaceAll("\\s\\r\\n\\s+", " ").replaceAll("\\r\\n\\s+", " ");
    }
    return s;
  }
}
