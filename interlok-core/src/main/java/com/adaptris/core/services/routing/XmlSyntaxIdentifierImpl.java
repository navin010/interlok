/*
 * Copyright 2015 Adaptris Ltd.
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

package com.adaptris.core.services.routing;

import javax.validation.Valid;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;

import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.core.util.DocumentBuilderFactoryBuilder;
import com.adaptris.core.util.XmlHelper;
import com.adaptris.util.KeyValuePairSet;
import com.adaptris.util.text.xml.SimpleNamespaceContext;
import com.adaptris.util.text.xml.XPath;

public abstract class XmlSyntaxIdentifierImpl extends SyntaxIdentifierImpl {

  @Valid
  private KeyValuePairSet namespaceContext;
  @AdvancedConfig
  @Valid
  private DocumentBuilderFactoryBuilder xmlDocumentFactoryConfig;

  public XmlSyntaxIdentifierImpl() {
    namespaceContext = new KeyValuePairSet();
  }

  protected Document createDocument(String message) {
    Document result = null;
    try {
      result = XmlHelper.createDocument(message, documentFactoryBuilder(SimpleNamespaceContext.create(namespaceContext)));
    } catch (Exception e) {
      // Can't be an XML Document
      result = null;
    }
    return result;
  }

  protected XPath createXPath() {
    NamespaceContext ctx = SimpleNamespaceContext.create(namespaceContext);
    return XPath.newXPathInstance(documentFactoryBuilder(ctx), ctx);
  }

  public KeyValuePairSet getNamespaceContext() {
    return namespaceContext;
  }

  public void setNamespaceContext(KeyValuePairSet kvps) {
    this.namespaceContext = kvps;
  }

  public DocumentBuilderFactoryBuilder getXmlDocumentFactoryConfig() {
    return xmlDocumentFactoryConfig;
  }


  public void setXmlDocumentFactoryConfig(DocumentBuilderFactoryBuilder xml) {
    this.xmlDocumentFactoryConfig = xml;
  }

  private DocumentBuilderFactoryBuilder documentFactoryBuilder(NamespaceContext nc) {
    return DocumentBuilderFactoryBuilder.newInstance(getXmlDocumentFactoryConfig(), nc);
  }
}
