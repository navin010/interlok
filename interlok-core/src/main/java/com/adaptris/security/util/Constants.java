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

package com.adaptris.security.util;

/** Constants.
 * @author lchan
 * @author $Author: lchan $
 */
public final class Constants {

  /**
   * System Property that tells us to bypass revocation checks.
   */
  public static final String IGNORE_REV = "adp.security.cert.ignore.revoked";

  /**
   * System Property that tells us that to ignore any revocation errors.
   * <p>
   * Revocation errors generally imply that we failed to get a CRL due to
   * network failure etc.
   * </p>
   */
  public static final String REV_CHECK_MUST_COMPLETE = "adp.security.cert."
      + "ignore.revoke.error";

  /**
   * System Property that can be set to be true to enable additional debug
   */
  public static final String CONFIG_ADDITIONAL_DEBUG = "adp.security.debug";


  /**
   * Name of the security provider.
   */
  public static final String SECURITY_PROVIDER = "BC";

  /**
   * URL Query key determining the keystore type.
   */
  public static final String KEYSTORE_TYPE = "keystoreType";

  /**
   * URL Query key determining the keystore password.
   */
  public static final String KEYSTORE_PASSWORD = "keystorePassword";

  /**
   * URL Query key determining the keystore alias
   */
  public static final String KEYSTORE_ALIAS = "keystoreAlias";

  /**
   * Boolean representation of CONFIG_ADDITIONAL_DEBUG
   * @see #CONFIG_ADDITIONAL_DEBUG
   */
  public static final boolean DEBUG = Boolean.valueOf(
      System.getProperty(CONFIG_ADDITIONAL_DEBUG, "false"))
      .booleanValue();

  /**
   * Custom keystore types
   */
  public static final String KEYSTORE_XMLKEYINFO = "XMLKEYINFO";

  /**
   * Custom keystore types
   */
  public static final String KEYSTORE_PKCS12 = "PKCS12";

  /**
   * Custom keystore types
   */
  public static final String KEYSTORE_X509 = "X509";

  private Constants() {
  }
}
