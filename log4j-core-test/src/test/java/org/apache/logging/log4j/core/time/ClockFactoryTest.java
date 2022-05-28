/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.time;

import org.apache.logging.log4j.core.time.internal.CachedClock;
import org.apache.logging.log4j.core.time.internal.CoarseCachedClock;
import org.apache.logging.log4j.core.time.internal.SystemClock;
import org.apache.logging.log4j.plugins.di.DI;
import org.apache.logging.log4j.plugins.di.Injector;
import org.apache.logging.log4j.test.junit.TestProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.assertj.core.api.Assertions.assertThat;

@DisabledOnOs(value = OS.WINDOWS, disabledReason = "https://issues.apache.org/jira/browse/LOG4J2-3515")
public class ClockFactoryTest {

    private final Injector injector = DI.createInjector();

    @Test
    public void testDefaultIsSystemClock() {
        injector.init();
        assertThat(injector.getInstance(Clock.class)).isInstanceOf(SystemClock.class);
    }

    @Test
    @TestProperties("log4j.Clock = SystemClock")
    public void testSpecifySystemClockShort() {
        injector.init();
        assertThat(injector.getInstance(Clock.class)).isInstanceOf(SystemClock.class);
    }

    @Test
    @TestProperties("log4j.Clock = org.apache.logging.log4j.core.time.internal.SystemClock")
    public void testSpecifySystemClockLong() {
        injector.init();
        assertThat(injector.getInstance(Clock.class)).isInstanceOf(SystemClock.class);
    }

    @Test
    @TestProperties("log4j.Clock = CachedClock")
    public void testSpecifyCachedClockShort() {
        injector.init();
        assertThat(injector.getInstance(Clock.class)).isInstanceOf(CachedClock.class);
    }

    @Test
    @TestProperties("log4j.Clock = org.apache.logging.log4j.core.time.internal.CachedClock")
    public void testSpecifyCachedClockLong() {
        injector.init();
        assertThat(injector.getInstance(Clock.class)).isInstanceOf(CachedClock.class);
    }

    @Test
    @TestProperties("log4j.Clock = CoarseCachedClock")
    public void testSpecifyCoarseCachedClockShort() {
        injector.init();
        assertThat(injector.getInstance(Clock.class)).isInstanceOf(CoarseCachedClock.class);
    }

    @Test
    @TestProperties("log4j.Clock = org.apache.logging.log4j.core.time.internal.CoarseCachedClock")
    public void testSpecifyCoarseCachedClockLong() {
        injector.init();
        assertThat(injector.getInstance(Clock.class)).isInstanceOf(CoarseCachedClock.class);
    }

    public static class MyClock implements Clock {
        @Override
        public long currentTimeMillis() {
            return 42;
        }
    }

    @Test
    @TestProperties("log4j.Clock = org.apache.logging.log4j.core.time.ClockFactoryTest$MyClock")
    public void testCustomClock() {
        injector.init();
        assertThat(injector.getInstance(Clock.class)).isInstanceOf(MyClock.class);
    }

}