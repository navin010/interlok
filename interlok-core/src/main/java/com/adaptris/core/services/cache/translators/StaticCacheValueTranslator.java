package com.adaptris.core.services.cache.translators;

import org.hibernate.validator.constraints.NotBlank;

import com.adaptris.annotation.InputFieldHint;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.services.cache.CacheKeyTranslator;
import com.adaptris.core.services.cache.CacheValueTranslator;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Implementation of {@link CacheValueTranslator} that could be useful for resolving the key when checking or retrieving from a
 * cache.
 * 
 * @config static-cache-value-translator
 */
@XStreamAlias("static-cache-value-translator")
public class StaticCacheValueTranslator implements CacheValueTranslator<String>, CacheKeyTranslator {

  @NotBlank
  @InputFieldHint(expression = true)
  private String value;

  public StaticCacheValueTranslator() {

  }

  public StaticCacheValueTranslator(String s) {
    this();
    setValue(s);
  }

  /**
   * @return the configured value in {@link #setValue(String)}
   */
  @Override
  public String getValueFromMessage(AdaptrisMessage msg) throws CoreException {
    return msg.resolve(value);
  }

  /**
   * @throws UnsupportedOperationException this method is not implemented for this translator
   */
  @Override
  public void addValueToMessage(AdaptrisMessage msg, String value) throws CoreException {
    throw new UnsupportedOperationException("StaticCacheValueTranslator can't add things to a message.");
  }

  /**
   * Sets the static value.
   *
   * @param s the value, which supports the {@code %message{}} syntax to resolve metadata.
   */
  public void setValue(String s) {
    value = s;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String getKeyFromMessage(AdaptrisMessage msg) throws CoreException {
    return getValueFromMessage(msg);
  }

}
