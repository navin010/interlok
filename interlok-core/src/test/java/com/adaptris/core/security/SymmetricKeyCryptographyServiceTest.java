package com.adaptris.core.security;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.ServiceCase;
import com.adaptris.core.common.ConstantDataInputParameter;
import com.adaptris.util.text.Conversion;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author mwarman
 */
public class SymmetricKeyCryptographyServiceTest  extends ServiceCase {

  private static final String ALGORITHM = "AES";
  private static final String CIPHER = "AES/CBC/PKCS5Padding";

  private static final String PAYLOAD = "Hello World";

  private byte[] key;
  private byte[] iv;
  private byte[] encryptedPayload;

  @Before
  public void setUp() throws Exception {
    key = generateRandomEncodedByteArray(32);
    iv = generateRandomEncodedByteArray(16);
    encryptedPayload = encrypt(PAYLOAD, key, iv);
  }

  @Test
  public void testDoService() throws Exception {
    SymmetricKeyCryptographyService service = new SymmetricKeyCryptographyService();
    service.setAlgorithm(ALGORITHM);
    service.setCipherTransformation(CIPHER);
    service.setKey(new ConstantDataInputParameter(Conversion.byteArrayToBase64String(key)));
    service.setInitialVector(new ConstantDataInputParameter(Conversion.byteArrayToBase64String(iv)));
    AdaptrisMessage message = AdaptrisMessageFactory.getDefaultInstance().newMessage(encryptedPayload);
    service.doService(message);
    assertEquals(PAYLOAD, message.getContent());
  }

  @Test
  public void testDoServiceEncrypt() throws Exception {
    SymmetricKeyCryptographyService service = new SymmetricKeyCryptographyService();
    service.setAlgorithm(ALGORITHM);
    service.setCipherTransformation(CIPHER);
    service.setOperationMode(SymmetricKeyCryptographyService.OpMode.ENCRYPT);
    service.setKey(new ConstantDataInputParameter(Conversion.byteArrayToBase64String(key)));
    service.setInitialVector(new ConstantDataInputParameter(Conversion.byteArrayToBase64String(iv)));
    AdaptrisMessage message = AdaptrisMessageFactory.getDefaultInstance().newMessage(PAYLOAD);
    service.doService(message);
    assertTrue(Arrays.equals(encryptedPayload, message.getPayload()));
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    return new SymmetricKeyCryptographyService()
        .withAlgorithm(ALGORITHM)
        .withCipherTransformation(CIPHER)
        .withKey(new ConstantDataInputParameter(Conversion.byteArrayToBase64String(generateRandomEncodedByteArray(32))))
        .withInitialVector(new ConstantDataInputParameter(Conversion.byteArrayToBase64String(generateRandomEncodedByteArray(16))));
  }

  private byte[] generateRandomEncodedByteArray(int size) {
    SecureRandom r = new SecureRandom();
    byte[] byteArray = new byte[size];
    r.nextBytes(byteArray);
    return byteArray;
  }

  private byte[] encrypt(String value, byte[] key, byte[] iv) throws Exception {
    Cipher cipher = Cipher.getInstance(CIPHER);
    SecretKeySpec secretKeySpecy = new SecretKeySpec(key, ALGORITHM);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpecy, ivParameterSpec);
    return cipher.doFinal(value.getBytes());
  }
}