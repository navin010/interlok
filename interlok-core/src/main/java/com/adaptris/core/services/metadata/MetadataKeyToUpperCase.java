/*
 * Copyright Adaptris Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adaptris.core.services.metadata;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.core.ServiceException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Implementation of {@link ReformatMetadataKey} that converts keys to upper case.
 * 
 * 
 * @author lchan
 *
 */
@XStreamAlias("metadata-key-to-upper-case")
@AdapterComponent
@ComponentProfile(summary = "Changes matching metadata keys to uppercase", tag = "service,metadata")
@DisplayOrder(order = {"keysToModify"})
public class MetadataKeyToUpperCase extends ReformatMetadataKey {

  @Override
  protected String reformatKey(String s) throws ServiceException {
    return s.toUpperCase();
  }

}
