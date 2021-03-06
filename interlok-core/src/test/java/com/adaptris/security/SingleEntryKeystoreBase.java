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
import java.security.cert.Certificate;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.adaptris.security.exc.KeystoreException;
import com.adaptris.security.keystore.KeystoreFactory;
import com.adaptris.security.keystore.KeystoreLocation;
import com.adaptris.security.keystore.KeystoreProxy;

import junit.framework.TestCase;

/**
 * Test Keystore Functionality wrapping a single KEYSTORE_PKCS12 certificate
 * 
 * @author lchan
 */
public abstract class SingleEntryKeystoreBase extends TestCase {
  protected KeystoreLocation kloc = null;
  protected Properties cfg;
  protected Config config;
  protected transient Log logR = null;

  /** @see TestCase */
  public SingleEntryKeystoreBase(String testName) {
    super(testName);
    logR = LogFactory.getLog(this.getClass());
  }

  /**
   * Get a certificate out of the keystore.
   */
  public void testContainsNonExistentAlias() throws Exception {
    KeystoreProxy ksp = KeystoreFactory.getDefault().create(kloc);
    ksp.load();

    String alias = String.valueOf(new Random().nextInt());
    if (ksp.containsAlias(alias)) {
      fail(alias + " exists in the specified keystore!");
    }
  }

  /**
   * Get the underlying keystore object
   */
  public void testKeystoreGetKeyStore() {
    try {
      KeystoreProxy ksp = KeystoreFactory.getDefault().create(kloc);
      ksp.load();
      assertNotNull("Keystore should not be null", ksp.getKeystore());
    }
    catch (Exception e) {
      logR.error(e.getMessage(), e);
      fail(e.getMessage());
    }
  }

  public void testImportCertificate() throws Exception {
    KeystoreProxy ksp = KeystoreFactory.getDefault().create(kloc);
    ksp.load();
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
    KeystoreProxy ksp = KeystoreFactory.getDefault().create(kloc);
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
    KeystoreProxy ksp = KeystoreFactory.getDefault().create(kloc);
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
