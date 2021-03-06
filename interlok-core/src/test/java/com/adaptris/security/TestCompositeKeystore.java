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

package com.adaptris.security;

import java.io.File;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.adaptris.security.exc.KeystoreException;
import com.adaptris.security.keystore.CompositeKeystore;
import com.adaptris.security.keystore.KeystoreFactory;
import com.adaptris.security.keystore.KeystoreLocation;

import junit.framework.TestCase;

/**
 * Test Composite Keystore Functionality.
 * 
 * @author $Author: lchan $
 */
public class TestCompositeKeystore extends TestCase {
  private Properties cfg;
  private Config config;
  private static Log logR = null;
  private List keystoreLocationList;

  /** @see TestCase */
  public TestCompositeKeystore(String testName) {
    super(testName);
    if (logR == null) {
      logR = LogFactory.getLog(TestCompositeKeystore.class);
    }
  }

  /**
   * @see TestCase#setUp()
   */
  public void setUp() throws Exception {
    super.setUp();
    config = Config.getInstance();
    cfg = config.getProperties();

    if (cfg == null) {
      fail("No Configuration(!) available");
    }
    keystoreLocationList = new ArrayList();
    config
        .buildKeystore(cfg.getProperty(Config.KEYSTORE_TEST_URL), null, false);
    Properties p = config.getPropertySubset(Config.KEYSTORE_COMPOSITE_URLROOT);
    for (Iterator i = p.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();

      KeystoreLocation kloc = KeystoreFactory.getDefault().create(
          cfg.getProperty(key),
          cfg.getProperty(Config.KEYSTORE_COMMON_KEYSTORE_PW).toCharArray());
      keystoreLocationList.add(kloc);
    }
  }

  /**
   * @see TestCase#tearDown()
   */
  public void tearDown() throws Exception {
    super.tearDown();
  }

  public void testKeystoreSize() throws Exception {
    CompositeKeystore composite = new CompositeKeystore(keystoreLocationList);
    assertEquals("Composite Keystore size", 3, composite.size());
  }

  /**
   * Get a certificate out of the keystore.
   */
  public void testContainsAlias() throws Exception {
    CompositeKeystore composite = new CompositeKeystore(keystoreLocationList);
    String alias = cfg.getProperty(Config.KEYSTORE_COMMON_PRIVKEY_ALIAS);
    if (!composite.containsAlias(alias)) {
      fail(alias + " doesn't exist in the specified keystore!");
    }
  }

  /**
   * Get a certificate out of the keystore.
   */
  public void testContainsNonExistentAlias() throws Exception {
    CompositeKeystore composite = new CompositeKeystore(keystoreLocationList);
    String alias = String.valueOf((new Random()).nextInt());
    if (composite.containsAlias(alias)) {
      fail(alias + " exists in the specified keystore!");
    }
  }

  public void testKeystoreGetCertificate() throws Exception {
    CompositeKeystore composite = new CompositeKeystore(keystoreLocationList);
    String alias = cfg.getProperty(Config.KEYSTORE_COMMON_PRIVKEY_ALIAS);
    if (composite.containsAlias(alias)) {
      Certificate thisCert = composite.getCertificate(alias);
      logR.trace(thisCert);
    }
    else {
      fail(alias + " does not exist in the specified keystore");
    }
  }

  public void testKeystoreGetCertificateChain() throws Exception {
    CompositeKeystore composite = new CompositeKeystore(keystoreLocationList);
    String alias = cfg.getProperty(Config.KEYSTORE_COMMON_PRIVKEY_ALIAS);
    if (composite.containsAlias(alias)) {
      Certificate[] thisCert = composite.getCertificateChain(alias);
      assertTrue(thisCert.length > 0);
    }
    else {
      fail(alias + " does not exist in the specified keystore");
    }
  }

  public void testKeystoreGetPrivateKey() throws Exception {
    CompositeKeystore composite = new CompositeKeystore(keystoreLocationList);
    String alias = cfg.getProperty(Config.KEYSTORE_SINGLE_PKCS12_ALIAS);
    if (composite.containsAlias(alias)) {
      PrivateKey pk = composite.getPrivateKey(alias, cfg.getProperty(Config.KEYSTORE_COMMON_KEYSTORE_PW).toCharArray());
      logR.trace(pk);
    }
    else {
      fail(alias + " does not exit in keystore list");
    }
  }

  public void testKeystoreGetKeyStore() throws Exception {
    CompositeKeystore composite = new CompositeKeystore(keystoreLocationList);
    assertNull("Keystore should be null", composite.getKeystore());
  }

  public void testKeystoreGetPrivateKeyNoPassword() throws Exception {
    CompositeKeystore composite = new CompositeKeystore(keystoreLocationList);
    String alias = cfg.getProperty(Config.KEYSTORE_COMMON_PRIVKEY_ALIAS);
    if (composite.containsAlias(alias)) {
      PrivateKey pk = composite.getPrivateKey(alias, null);
      logR.trace(pk);
    }
    else {
      fail(alias + " does not exist in keystore list");
    }
  }

  public void testKeystoreAliasCaseBug890() throws Exception {
    CompositeKeystore composite = new CompositeKeystore();
    String x509Alias = cfg
        .getProperty(Config.KEYSTORE_SINGLE_X509_ALIAS_UPPERCASE);
    String x509KeyInfoAlias = cfg
        .getProperty(Config.KEYSTORE_SINGLE_XML_KEY_INFO_ALIAS_UPPERCASE);
    composite.addKeystore(KeystoreFactory.getDefault().create(
        cfg.getProperty(Config.KEYSTORE_SINGLE_X509_URL_UPPERCASE), null));
    composite.addKeystore(KeystoreFactory.getDefault().create(
        cfg.getProperty(Config.KEYSTORE_SINGLE_XML_KEY_INFO_URL_UPPERCASE),
        null));
    assertTrue(composite.containsAlias(x509Alias));
    assertTrue(composite.containsAlias(x509KeyInfoAlias));
    assertNotNull(composite.getCertificate(x509Alias));
    assertNotNull(composite.getCertificate(x509KeyInfoAlias));
  }

  public void testImportCertificate() throws Exception {
    CompositeKeystore ksp = new CompositeKeystore(keystoreLocationList);
    try {
      ksp.setCertificate("", "");
      fail("Import successful");
    }
    catch (Exception e) {
      assertEquals(KeystoreException.class, e.getClass());
    }
    try {
      ksp.setCertificate("", (Certificate) null);
      fail("Import successful");
    }
    catch (Exception e) {
      assertEquals(KeystoreException.class, e.getClass());
    }
    try {
      ksp.setCertificate("", (InputStream) null);
      fail("Import successful");
    }
    catch (Exception e) {
      assertEquals(KeystoreException.class, e.getClass());
    }
    try {
      ksp.setCertificate("", (File) null);
      fail("Import successful");
    }
    catch (Exception e) {
      assertEquals(KeystoreException.class, e.getClass());
    }
  }

  public void testImportCertificateChain() throws Exception {
    CompositeKeystore ksp = new CompositeKeystore(keystoreLocationList);
    ksp.load();
    try {
      ksp.importCertificateChain("", "".toCharArray(), "");
      fail("Import successful");
    }
    catch (Exception e) {
      assertEquals(KeystoreException.class, e.getClass());
    }
    try {
      ksp.importCertificateChain("", "".toCharArray(), (InputStream) null);
      fail("Import successful");
    }
    catch (Exception e) {
      assertEquals(KeystoreException.class, e.getClass());
    }
    try {
      ksp.importCertificateChain("", "".toCharArray(), (File) null);
      fail("Import successful");
    }
    catch (Exception e) {
      assertEquals(KeystoreException.class, e.getClass());
    }
  }

  public void testImportPrivateKey() throws Exception {
    CompositeKeystore ksp = new CompositeKeystore(keystoreLocationList);
    ksp.load();
    try {
      ksp.importPrivateKey("", "".toCharArray(), "", "".toCharArray());
      fail("Import successful");
    }
    catch (Exception e) {
      assertEquals(KeystoreException.class, e.getClass());
    }
    try {
      ksp.importPrivateKey("", "".toCharArray(), (InputStream) null, ""
          .toCharArray());
      fail("Import successful");
    }
    catch (Exception e) {
      assertEquals(KeystoreException.class, e.getClass());
    }
    try {
      ksp.importPrivateKey("", "".toCharArray(), (File) null, "".toCharArray());
      fail("Import successful");
    }
    catch (Exception e) {
      assertEquals(KeystoreException.class, e.getClass());
    }

  }
}
