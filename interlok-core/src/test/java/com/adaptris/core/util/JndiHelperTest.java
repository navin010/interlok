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

package com.adaptris.core.util;

import java.util.ArrayList;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.adaptris.core.AdaptrisConnection;
import com.adaptris.core.BaseCase;
import com.adaptris.core.CoreException;
import com.adaptris.core.JndiContextFactory;
import com.adaptris.core.NullConnection;
import com.adaptris.core.jdbc.AdvancedJdbcPooledConnection;
import com.adaptris.core.jdbc.JdbcPooledConnection;
import com.adaptris.core.jdbc.PooledConnectionProperties;
import com.adaptris.core.transaction.DummyTransactionManager;
import com.adaptris.core.transaction.TransactionManager;
import com.adaptris.util.GuidGenerator;
import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;

public class JndiHelperTest extends BaseCase {
  private Properties env = new Properties();
  public JndiHelperTest(String s) {
    super(s);
  }

  @Override
  public void setUp() throws Exception {
    env.put(Context.INITIAL_CONTEXT_FACTORY, JndiContextFactory.class.getName());
  }

  @Override
  public void tearDown() throws Exception {

  }

  public void testBindCollection() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId(getName());
    ArrayList<AdaptrisConnection> connectionList = new ArrayList<AdaptrisConnection>();
    connectionList.add(connection);
    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(connectionList);
      NullConnection lookedup = (NullConnection) initialContext.lookup("adapter:comp/env/" + getName());
      assertNotNull(lookedup);
      assertTrue(lookedup instanceof NullConnection);
      assertEquals(getName(), lookedup.getUniqueId());
    }
    finally {
      JndiHelper.unbindQuietly(initialContext, connectionList, true);
    }
  }

  public void testBindCollection_Debug() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId(getName());
    ArrayList<AdaptrisConnection> connectionList = new ArrayList<AdaptrisConnection>();
    connectionList.add(connection);
    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(connectionList, true);
      NullConnection lookedup = (NullConnection) initialContext.lookup("adapter:comp/env/" + getName());
      assertNotNull(lookedup);
      assertTrue(lookedup instanceof NullConnection);
      assertEquals(getName(), lookedup.getUniqueId());
    }
    finally {
      JndiHelper.unbindQuietly(initialContext, connectionList, true);
    }
  }

  public void testBindCollection_WithContext() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId(getName());
    ArrayList<AdaptrisConnection> connectionList = new ArrayList<AdaptrisConnection>();
    connectionList.add(connection);
    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(initialContext, connectionList, true);
      NullConnection lookedup = (NullConnection) initialContext.lookup("adapter:comp/env/" + getName());
      assertNotNull(lookedup);
      assertTrue(lookedup instanceof NullConnection);
      assertEquals(getName(), lookedup.getUniqueId());
    }
    finally {
      JndiHelper.unbindQuietly(initialContext, connectionList, true);
    }
  }

  public void testBindObject() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId(getName());
    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(connection, false);
      NullConnection lookedup = (NullConnection) initialContext.lookup("adapter:comp/env/" + getName());
      assertNotNull(lookedup);
      assertEquals(getName(), lookedup.getUniqueId());
    }
    finally {
      JndiHelper.unbindQuietly(initialContext, connection, false);
    }
  }

  public void testBindObject_WithContext() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId(getName());
    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(initialContext, connection, false);
      NullConnection lookedup = (NullConnection) initialContext.lookup("adapter:comp/env/" + getName());
      assertNotNull(lookedup);
      assertEquals(getName(), lookedup.getUniqueId());
      // won't fail, should just get ignored.
      JndiHelper.bind(initialContext, (AdaptrisConnection) null, true);
      JndiHelper.bind(initialContext, (AdaptrisConnection) null, false);
    }
    finally {
      JndiHelper.unbindQuietly(initialContext, connection, false);
    }
  }

  public void testBindObject_WithScheme() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId("adapter:" + getName());
    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(initialContext, connection, false);
      NullConnection lookedup = (NullConnection) initialContext.lookup("adapter:comp/env/" + getName());
      assertNotNull(lookedup);
      assertEquals("adapter:" + getName(), lookedup.getUniqueId());
    }
    finally {
      JndiHelper.unbindQuietly(initialContext, connection, false);
    }
  }

  public void testBindObject_WithLookupName() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId("SomethingElseEntirely");
    connection.setLookupName("adapter:comp/env/" + getName());
    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(initialContext, connection, false);
      NullConnection lookedup = (NullConnection) initialContext.lookup("adapter:comp/env/" + getName());
      assertNotNull(lookedup);
      assertEquals("SomethingElseEntirely", lookedup.getUniqueId());
    }
    finally {
      JndiHelper.unbindQuietly(initialContext, connection, false);
    }
  }

  public void testBindObject_AlreadyBound() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId(getName());
    InitialContext initialContext = new InitialContext(env);
    JndiHelper.bind(initialContext, connection, false);
    try {
      JndiHelper.bind(initialContext, connection, true);
      fail();
    }
    catch (CoreException expected) {
      ;
    }
    try {
      JndiHelper.bind(initialContext, connection, false);
      fail();
    }
    catch (CoreException expected) {
      ;
    }
    JndiHelper.unbindQuietly(initialContext, connection, false);
  }
  
  public void testBindTransactionManager_AlreadyBound() throws Exception {
    TransactionManager transactionManager = new DummyTransactionManager(getName(), null);
    InitialContext initialContext = new InitialContext(env);
    JndiHelper.bind(initialContext, transactionManager, false);
    try {
      JndiHelper.bind(initialContext, transactionManager, true);
      fail();
    }
    catch (CoreException expected) {
      ;
    }
    try {
      JndiHelper.bind(initialContext, transactionManager, false);
      fail();
    }
    catch (CoreException expected) {
      ;
    }
    JndiHelper.unbindQuietly(initialContext, transactionManager, false);
  }
  
  public void testBindNullTransactionManager() throws Exception {
    TransactionManager transactionManager = null;
    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(initialContext, transactionManager, false);
    }
    catch (CoreException expected) {
      fail("Should not error, just ignore.");
    }
  }
  
  public void testBindNullTransactionManager_Debug() throws Exception {
    TransactionManager transactionManager = null;
    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(initialContext, transactionManager, true);
    }
    catch (CoreException expected) {
      fail("Should not error, just ignore.");
    }
  }
  
  public void testUnbindNullTransactionManager() throws Exception {
    TransactionManager transactionManager = null;
    try {
      JndiHelper.unbind(transactionManager, false);
    }
    catch (CoreException expected) {
      fail("Should not error, just ignore.");
    }
  }
  
  public void testUnbindNullTransactionManager_Debug() throws Exception {
    TransactionManager transactionManager = null;
    try {
      JndiHelper.unbind(transactionManager, true);
    }
    catch (CoreException expected) {
      fail("Should not error, just ignore.");
    }
  }
  
  public void testUnbindUnboundTransactionManager() throws Exception {
    TransactionManager transactionManager = new DummyTransactionManager(getName(), null);
    try {
      JndiHelper.unbind(transactionManager, false);
      fail();
    }
    catch (CoreException expected) {
      // not previously bound, so should error.
    }
  }
  
  public void testUnbindUnboundTransactionManager_Debug() throws Exception {
    TransactionManager transactionManager = new DummyTransactionManager(getName(), null);
    try {
      JndiHelper.unbind(transactionManager, true);
      fail();
    }
    catch (CoreException expected) {
      // not previously bound, so should error.
    }
  }

  public void testBindJdbcConnection() throws Exception {
    JdbcPooledConnection connection = new JdbcPooledConnection();
    connection.setConnectUrl("jdbc:derby:memory:" + new GuidGenerator().safeUUID() + ";create=true");
    connection.setDriverImp("org.apache.derby.jdbc.EmbeddedDriver");
    connection.setMinimumPoolSize(1);
    connection.setAcquireIncrement(1);
    connection.setMaximumPoolSize(7);
    connection.setUniqueId(getName());
    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(initialContext, connection, true);
      JdbcPooledConnection lookedup = (JdbcPooledConnection) initialContext.lookup("adapter:comp/env/" + getName());
      assertEquals(getName(), lookedup.getUniqueId());
      assertNotNull(initialContext.lookup("adapter:comp/env/jdbc/" + getName()));
    }
    finally {
      JndiHelper.unbindQuietly(initialContext, connection, true);
    }
    try {
      JndiHelper.bind(initialContext, connection, false);
      JdbcPooledConnection lookedup = (JdbcPooledConnection) initialContext.lookup("adapter:comp/env/" + getName());
      assertEquals(getName(), lookedup.getUniqueId());
      assertNotNull(initialContext.lookup("adapter:comp/env/jdbc/" + getName()));
    }
    finally {
      JndiHelper.unbindQuietly(initialContext, connection, false);
    }
  }

  public void testBindJdbcConnection_WithLookupName() throws Exception {
    JdbcPooledConnection connection = new JdbcPooledConnection();
    connection.setConnectUrl("jdbc:derby:memory:" + new GuidGenerator().safeUUID() + ";create=true");
    connection.setDriverImp("org.apache.derby.jdbc.EmbeddedDriver");
    connection.setMinimumPoolSize(1);
    connection.setAcquireIncrement(1);
    connection.setMaximumPoolSize(7);
    connection.setUniqueId("jdbcConnectionLookup");
    connection.setLookupName("adapter:comp/env/" + getName());

    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(initialContext, connection, true);
      JdbcPooledConnection lookedup = (JdbcPooledConnection) initialContext.lookup("adapter:comp/env/" + getName());
      assertEquals("jdbcConnectionLookup", lookedup.getUniqueId());
      assertNotNull(initialContext.lookup("adapter:comp/env/jdbc/" + getName()));
    }
    finally {
      JndiHelper.unbindQuietly(initialContext, connection, true);
    }
  }
  
  public void testBindAdvancedJdbcConnection_WithLookupName() throws Exception {
    AdvancedJdbcPooledConnection connection = new AdvancedJdbcPooledConnection();
    connection.setConnectUrl("jdbc:derby:memory:" + new GuidGenerator().safeUUID() + ";create=true");
    connection.setDriverImp("org.apache.derby.jdbc.EmbeddedDriver");
    KeyValuePairSet poolProps = new KeyValuePairSet();
    poolProps.add(new KeyValuePair(PooledConnectionProperties.minPoolSize.name(), "1"));
    poolProps.add(new KeyValuePair(PooledConnectionProperties.acquireIncrement.name(), "1"));
    poolProps.add(new KeyValuePair(PooledConnectionProperties.maxPoolSize.name(), "7"));
    connection.setConnectionPoolProperties(poolProps);
    connection.setUniqueId("jdbcAdvConnectionLookup");
    connection.setLookupName("adapter:comp/env/" + getName());

    InitialContext initialContext = new InitialContext(env);
    try {
      JndiHelper.bind(initialContext, connection, true);
      AdvancedJdbcPooledConnection lookedup = (AdvancedJdbcPooledConnection) initialContext.lookup("adapter:comp/env/" + getName());
      assertEquals("jdbcAdvConnectionLookup", lookedup.getUniqueId());
      assertNotNull(initialContext.lookup("adapter:comp/env/jdbc/" + getName()));
    }
    finally {
      JndiHelper.unbindQuietly(initialContext, connection, true);
    }
  }

  public void testUnbindCollection() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId(getName());
    ArrayList<AdaptrisConnection> connectionList = new ArrayList<AdaptrisConnection>();
    connectionList.add(connection);
    JndiHelper.bind(connectionList);
    JndiHelper.unbind(connectionList, false);
    try {
      JndiHelper.unbind(connectionList, false);
      fail();
    }
    catch (CoreException expected) {

    }
  }

  public void testUnbindCollection_Debug() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId(getName());
    ArrayList<AdaptrisConnection> connectionList = new ArrayList<AdaptrisConnection>();
    connectionList.add(connection);
    InitialContext initialContext = new InitialContext(env);
    JndiHelper.bind(connectionList);
    JndiHelper.unbind(connectionList, true);
    try {
      JndiHelper.unbind(connectionList, true);
      fail();
    }
    catch (CoreException expected) {

    }
  }

  public void testUnbindCollection_WithContext() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId(getName());
    ArrayList<AdaptrisConnection> connectionList = new ArrayList<AdaptrisConnection>();
    connectionList.add(connection);
    InitialContext initialContext = new InitialContext(env);
    JndiHelper.bind(initialContext, connectionList, true);
    JndiHelper.unbind(initialContext, connectionList, true);
    try {
      JndiHelper.unbind(initialContext, connectionList, true);
      fail();
    }
    catch (CoreException expected) {

    }
  }

  public void testUnbindObject() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId(getName());
    InitialContext initialContext = new InitialContext(env);
    JndiHelper.bind(connection, false);
    JndiHelper.unbind(connection, false);
  }

  public void testUnbindObject_WithContext() throws Exception {
    NullConnection connection = new NullConnection();
    connection.setUniqueId(getName());
    InitialContext initialContext = new InitialContext(env);
    JndiHelper.bind(initialContext, connection, false);
    JndiHelper.unbind(initialContext, (AdaptrisConnection) null, true);
    JndiHelper.unbind(initialContext, (AdaptrisConnection) null, false);
    JndiHelper.unbind(initialContext, connection, false);
  }

  public void testUnbindJdbcConnection() throws Exception {
    JdbcPooledConnection connection = new JdbcPooledConnection();
    connection.setConnectUrl("jdbc:derby:memory:" + new GuidGenerator().safeUUID() + ";create=true");
    connection.setDriverImp("org.apache.derby.jdbc.EmbeddedDriver");
    connection.setMinimumPoolSize(1);
    connection.setAcquireIncrement(1);
    connection.setMaximumPoolSize(7);
    connection.setUniqueId(getName());
    InitialContext initialContext = new InitialContext(env);
    JndiHelper.bind(initialContext, connection, true);
    JndiHelper.unbind(initialContext, connection, true);
    JndiHelper.bind(initialContext, connection, false);
    JndiHelper.unbind(initialContext, connection, false);
  }

}
