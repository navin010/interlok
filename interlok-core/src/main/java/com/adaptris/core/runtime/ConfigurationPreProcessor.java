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

package com.adaptris.core.runtime;

import com.adaptris.annotation.Removal;

/**
 * <p>
 * Implementations of this interface will perform actions on the xml configuration before the
 * configuration is unmarshalled.
 * </p>
 * 
 * @author amcgrath
 * @deprecated will be removed in 3.9.0
 */
@Deprecated
@Removal(version = "3.9.0")
public interface ConfigurationPreProcessor extends com.adaptris.core.config.ConfigPreProcessor {

}
