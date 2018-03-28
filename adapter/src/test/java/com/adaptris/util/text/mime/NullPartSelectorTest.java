/*
 * Copyright 2018 Adaptris Ltd.
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
package com.adaptris.util.text.mime;

import static com.adaptris.util.text.mime.PartIteratorCase.createMultipart;
import static com.adaptris.util.text.mime.PartIteratorCase.generateByteArrayInput;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NullPartSelectorTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testSelectMultiPartInput() throws Exception {
    MultiPartInput input = new MultiPartInput(generateByteArrayInput(false), false);
    NullPartSelector selector = new NullPartSelector();
    MimeBodyPart part = selector.select(input);
    assertNotNull(part);
  }

  @Test
  public void testSelectMimeMultipart() throws Exception {
    MimeMultipart mmp = createMultipart();
    NullPartSelector selector = new NullPartSelector();
    List<MimeBodyPart> parts = selector.select(mmp);
    assertEquals(3, parts.size());
  }

  @Test
  public void testSelectBodyPartIterator() throws Exception {
    try (BodyPartIterator input = new BodyPartIterator(generateByteArrayInput(false))) {
      NullPartSelector selector = new NullPartSelector();
      MimeBodyPart part = selector.select(input);
      assertNotNull(part);
    }
  }
}
