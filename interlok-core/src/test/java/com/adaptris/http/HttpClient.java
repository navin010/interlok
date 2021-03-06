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

package com.adaptris.http;

import java.net.InetSocketAddress;
import java.net.Socket;

import com.adaptris.util.URLString;

/**A simple HTTP client.
 * <p>This is the concrete sub-class for HttpClientTransport.  A raw-socket
 * connection
 * is created between the client and the remote server using java.net.Socket
 * @see HttpClientTransport
 * @see HttpHeaders
 */
public class HttpClient extends HttpClientTransport {
  /** @see Object#Object()
   */
  public HttpClient() {
    super();
  }

  /** Constructor.
   *  @param url the URL to connect to
   *  @throws HttpException on Error.
   */
  public HttpClient(String url) throws HttpException {
    super(url);
  }

  /** @see HttpClientTransport#canHandle(String)
   */
  @Override
  protected boolean canHandle(String url) {
    URLString urlString = new URLString(url);
    if (urlString.getProtocol() == null
      || !urlString.getProtocol().equalsIgnoreCase("http")) {
      return false;
    }
    return true;
  }

  /**
   *
   * @see com.adaptris.http.HttpClientTransport#createConnection(int)
   */
  @Override
  protected Socket createConnection(int timeout) throws HttpException {

    Socket socket = null;
    try {
      if (logR.isTraceEnabled()) {
        logR.trace("Connecting to " + this.getHost() + ":" + this.getPort());
      }
      socket = new Socket();
      InetSocketAddress addr = new InetSocketAddress(this.getHost(), (this.getPort() == -1 ? 80 : this.getPort()));
      socket.connect(addr, timeout);
      socket.setSoTimeout(timeout);
    } catch (Exception e) {
      throw new HttpException(e);
    }
    return socket;
  }

  /**
   *  @see com.adaptris.http.HttpClientTransport#getPort()
   */
  @Override
  public int getPort() {
    return currentUrl.getPort() == -1 ? 80 : currentUrl.getPort();
  }
}
