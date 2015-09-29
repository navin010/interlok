package com.adaptris.core.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * {@link ParameterHandler} implementation that ignores HTTP headers.
 * 
 * @config http-ignore-headers
 * @deprecated since 3.0.6 use {@link com.adaptris.core.http.jetty.NoOpHeaderHandler} instead.
 */
@XStreamAlias("http-ignore-headers")
@Deprecated
public class NoOpHeaderHandler extends com.adaptris.core.http.jetty.NoOpHeaderHandler {

  private static transient boolean warningLogged;
  private transient Logger log = LoggerFactory.getLogger(this.getClass());

  public NoOpHeaderHandler() {
    super();
    if (!warningLogged) {
      log.warn("[{}] is deprecated, use [{}] instead", this.getClass().getSimpleName(),
          com.adaptris.core.http.jetty.NoOpHeaderHandler.class.getName());
      warningLogged = true;
    }
  }
}