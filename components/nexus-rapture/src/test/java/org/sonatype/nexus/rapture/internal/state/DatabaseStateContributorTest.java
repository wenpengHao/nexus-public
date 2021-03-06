/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.rapture.internal.state;

import java.util.Map;

import org.sonatype.goodies.testsupport.TestSupport;
import org.sonatype.nexus.orient.freeze.DatabaseFreezeService;
import org.sonatype.nexus.orient.freeze.ReadOnlyState;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class DatabaseStateContributorTest
    extends TestSupport
{

  DatabaseStateContributor underTest;

  @Mock
  DatabaseFreezeService databaseFreezeService;

  @Before
  public void setup() {
    underTest = new DatabaseStateContributor(databaseFreezeService);
  }

  @Test
  public void testGetState() {
    when(databaseFreezeService.getReadOnlyState()).thenReturn(state(true, "", false));
    assertThat(underTest.getState().get("db"), is(expected(true,"", false)));

    when(databaseFreezeService.getReadOnlyState()).thenReturn(state(false, "", false));
    assertThat(underTest.getState().get("db"), is(expected(false,"", false)));
  }

  ReadOnlyState state(boolean frozen, String message, boolean system) {
    return new ReadOnlyState()
    {
      @Override
      public boolean isFrozen() {
        return frozen;
      }

      @Override
      public String getSummaryReason() {
        return message;
      }

      @Override
      public boolean isSystemInitiated() {
        return system;
      }
    };
  }

  Map<String, Object> expected(boolean frozen, String message, boolean system) {
    return of("dbFrozen", frozen,
  "system", system,
  "reason", message
    );
  }
}
